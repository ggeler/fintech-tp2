package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.bet.Event;
import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.entity.state.event.EventStatusEnum;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.service.EventService;
import com.up.fintech.armagedon.tp4.service.TransactionService;

@Service
public final class BetRequestReceiveServiceStrategy implements ITransactionStrategy {

	private final EventService eventService;
	private final TransactionService transactionService;
	
	public BetRequestReceiveServiceStrategy(EventService eventService, TransactionService transactionService) {
		this.eventService = eventService;
		this.transactionService = transactionService;
	}
	
	private Transaction request(Wallet wallet, Bet bet) throws UserNotFoundException, WalletNotFoundException, TransactionException {
		var event = eventService.getEvent(bet.getEvent().getEventKey()).orElseGet( ()->eventService.save(bet.getEvent() ));
		event.setEventState();
		if (event.getStatus()==EventStatusEnum.CLOSED)
			throw new TransactionException("Evento cerrado para apuestas");
		bet.setEvent(event);
		bet.setWallet(wallet);
		bet.withdrawRequest();
		event.getBets().add(bet);
		eventService.save(event);
		return transactionService.save(bet);
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		
		return request(wallet, (Bet) transaction);
	}

}
