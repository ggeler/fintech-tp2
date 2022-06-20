package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.entity.debit.FeeCharge;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.service.EventService;

@Service
public final class BetPreviewReceiveServiceStrategy implements ITransactionStrategy {

	private final EventService eventService;
	
	public BetPreviewReceiveServiceStrategy(EventService eventService) {
		this.eventService = eventService;
	}
	
	private Transaction preview(Wallet wallet, Bet bet) throws UserNotFoundException, WalletNotFoundException, TransactionException {
		bet.setWallet(wallet);
		bet.withdrawRequest();
		//var event = eventService.getEvent(bet.getEvent().getEventKey()).orElse(eventService.save(bet.getEvent()));
//		bet.setEvent(event);
		return bet;
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		
		return preview(wallet, (Bet) transaction);
	}

}
