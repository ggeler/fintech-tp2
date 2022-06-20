package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.service.TransactionService;

@Service
public final class BetCancelServiceStrategy implements ITransactionStrategy {

	private final TransactionService transactionService;
	
	@Autowired
	public BetCancelServiceStrategy(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	private Transaction cancel(Wallet wallet, Bet bet) throws UserNotFoundException, WalletNotFoundException, TransactionException {
		bet.cancelWithdrawRequest();
		return transactionService.save(bet);
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		
		return cancel(wallet, (Bet) transaction);
	}

}
