package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.credit.BetBag;
import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.entity.state.event.EventStatusEnum;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.service.EventService;
import com.up.fintech.armagedon.tp4.service.TransactionService;
import com.up.fintech.armagedon.tp4.service.WalletService;

@Service
public final class BetConfirmServiceStrategy implements ITransactionStrategy {

	private final EventService eventService;
	private final TransactionService transactionService;
	private final WalletService walletService;
	
	public BetConfirmServiceStrategy(EventService eventService, TransactionService transactionService, WalletService walletService) {
		this.eventService = eventService;
		this.transactionService = transactionService;
		this.walletService = walletService;
	}
	
	private Transaction confirmation(Wallet wallet, Bet bet) throws UserNotFoundException, WalletNotFoundException, TransactionException {
		var event = eventService.getEvent(bet.getEvent().getEventKey()).orElse( eventService.save(bet.getEvent() ));
		event.setEventState();
		if (event.getStatus()==EventStatusEnum.CLOSED)
			throw new TransactionException("Evento cerrado para apuestas");
		wallet.confirmWithdrawRequest(bet);
		
		var betBagWallet =  walletService.getBetBagWallet();
		var betbag = new BetBag(bet, betBagWallet);
		
		betBagWallet.directDeposit(betbag);
		transactionService.save(betbag);
		eventService.save(event);
		return transactionService.save(bet);
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		
		return confirmation(wallet, (Bet) transaction);
	}

}
