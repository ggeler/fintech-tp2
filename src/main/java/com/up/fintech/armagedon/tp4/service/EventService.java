package com.up.fintech.armagedon.tp4.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.up.fintech.armagedon.tp4.dto.EventTransferDto;
import com.up.fintech.armagedon.tp4.entity.bet.Event;
import com.up.fintech.armagedon.tp4.entity.credit.FeePayment;
import com.up.fintech.armagedon.tp4.entity.credit.PayBet;
import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.entity.debit.DebitBet;
import com.up.fintech.armagedon.tp4.entity.debit.FeeCharge;
import com.up.fintech.armagedon.tp4.entity.state.transaction.OpenBetState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.TransactionStatusEnum;
import com.up.fintech.armagedon.tp4.misc.error.EventException;
import com.up.fintech.armagedon.tp4.repository.IEventRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EventService {

	private final IEventRepository repository;
	private final WalletService walletService;
	private final TransactionService transactionService;
	List<Bet> winners = new ArrayList<>();
	int cant=0;
	
	public EventService(IEventRepository repository, WalletService walletService, TransactionService transactionService) {
		this.repository = repository;
		this.walletService = walletService;
		this.transactionService = transactionService;
	}
	
	public Optional<Event> getEvent(long eventKey) {
		var event = repository.findByEventKey(eventKey);
		return event;
	}

	public Event save(Event event) {
		return repository.save(event);
	}
	
	public Optional<Event> saveOptional(Event event) {
		return Optional.of(repository.save(event));
	}
	
	@Transactional(label = "EventTransaction", isolation = Isolation.REPEATABLE_READ)
	public void update(EventTransferDto dto) throws EventException {
		var event = getEvent(dto.getEventKey()).orElseThrow(()-> new EventException("Juego no encontrado"));
		var winnerKey = event.getAwayTeamScore()>event.getHomeTeamScore() ? event.getAwayTeamKey() : event.getHomeTeamKey();
		
		var bets = event.getBets().stream()
				.filter(bet -> bet.getStatus()==TransactionStatusEnum.OPEN)
				.filter(bet -> bet.getEvent().getEventKey()==dto.getEventKey())
				.collect(Collectors.toList());
		boolean even=false, away=false, home=false;
			
		
		event.setAwayTeamScore(dto.getAwayTeamResult());
		event.setHomeTeamScore(dto.getHomeTeamResult());
		repository.save(event);
		
		if (event.getAwayTeamScore()==event.getHomeTeamScore())
			even=true;
		else if (event.getAwayTeamScore()>event.getHomeTeamScore()){
			away=true;
		} else
			home=true;
		
		
		
		if (!even && !away && home)  //No hubo empate
			winners = bets.stream().filter(bet -> bet.getAwayTeamScore() < bet.getHomeTeamScore()).collect(Collectors.toList());
		else if (!even && away && !home)
			winners = bets.stream().filter(bet -> bet.getAwayTeamScore() > bet.getHomeTeamScore()).collect(Collectors.toList());
		else if (even && !away && home)
			winners = bets.stream().filter(bet -> bet.getAwayTeamScore() == bet.getHomeTeamScore()).collect(Collectors.toList());
		
		var losers = bets.stream()
				.filter(loser -> winners.stream().anyMatch(winner -> winner.getId()!=loser.getId()))
				.collect(Collectors.toList());
		
		var betbagWallet = walletService.getBetBagWallet();
		var feeWallet = walletService.getFeeWallet();
	
		winners.stream().forEach(winner -> { 
			winner.setTransactionState();
			
			var debit = new DebitBet(betbagWallet, winner.getAmount().multiply(BigDecimal.valueOf(2)), winner);
			betbagWallet.payWinningBet(debit);
			
			transactionService.save(debit);
			
			var credit = new PayBet(winner.getWallet(), winner.getAmount().multiply(BigDecimal.valueOf(2)), winner);
			winner.getWallet().receiveWinningBet(credit);
			transactionService.save(credit);
			
			var feeCharge = new FeeCharge(credit, winner.getWallet());
			winner.getWallet().directWithdraw(feeCharge);
			transactionService.save(feeCharge);
			
			var feePayment = new FeePayment(feeCharge, feeWallet);
			feeWallet.directDeposit(feePayment);
			transactionService.save(feePayment);
			
			((OpenBetState) winner.getState()).win();
			transactionService.save(winner);
			
			walletService.save(betbagWallet);
			walletService.save(winner.getWallet());
			walletService.save(feeWallet);
			cant++;
			log.info("Winner: "+winner.getId()+" waller: "+winner.getWallet().getId());
			log.info("Amount: "+winner.getAmount()+" fee: "+feeCharge.getTotal() +" Total: "+winner.getTotal());
		}); 
		log.info("Cantidad winners: "+cant);
		cant = 0;
		losers.stream().forEach(loser -> {
			loser.setTransactionState();
			((OpenBetState) loser.getState()).lose();
			transactionService.save(loser);
			log.info("Loser: "+loser.getId()+" waller: "+loser.getWallet().getId()+" Bet: "+loser.getAmount());
			cant++;
		});
		log.info("Cantidad losers: "+cant);
		event.getState().changeState();
		repository.save(event);
//		betbagWallet.payWinningBet(new PayBet(winner.getWallet(), winner.getAmount())));
		
		
	}
}
