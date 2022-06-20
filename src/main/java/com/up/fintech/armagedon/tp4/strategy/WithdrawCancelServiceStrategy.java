package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.service.TransactionService;
import com.up.fintech.armagedon.tp4.service.WalletService;

@Service
public final class WithdrawCancelServiceStrategy implements ITransactionStrategy {

	private final WalletService service;
	private final TransactionService repository;
	
	@Autowired
	public WithdrawCancelServiceStrategy(WalletService service, TransactionService repository) {
		this.service = service;
		this.repository = repository;
	}
	
	private Transaction cancelWithdraw(Wallet wallet, Withdraw withdraw) throws UserNotFoundException, WalletNotFoundException, TransactionException {
		withdraw.setWallet(wallet);
		try {
			withdraw.cancelWithdrawRequest();
			var fee = withdraw.getFeeTransaction();
			fee.setTransactionState();
			var savedDeposit = repository.save(withdraw);
			service.save(wallet);
			fee.cancelWithdrawRequest();
			repository.save(fee);
			return savedDeposit;
		} catch (TransactionException e) {
			throw e;
		} 
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return cancelWithdraw(wallet, (Withdraw) transaction);
	}

}
