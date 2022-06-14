package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.Debit;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;
import com.up.fintech.armagedon.tp4.service.WalletService;

@Service
public final class WithdrawConfirmationServiceStrategy implements ITransactionStrategy {

	private final WalletService service;
	private final ITransactionRepository repository;
	
	@Autowired
	public WithdrawConfirmationServiceStrategy(WalletService service, ITransactionRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	
	private Transaction confirmWithdraw(Wallet wallet, Debit withdraw) throws UserNotFoundException, WalletNotFoundException, TransactionException {
		withdraw.setWallet(wallet);
		try {
			wallet.confirmWithdrawRequest(withdraw);
			var savedDeposit = repository.save(withdraw);
			service.save(wallet);
			return savedDeposit;
		} catch (TransactionException e) {
			throw e;
		} 
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return confirmWithdraw(wallet, (Debit) transaction);
	}

}