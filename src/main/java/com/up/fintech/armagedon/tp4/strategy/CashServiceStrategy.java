package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;
import com.up.fintech.armagedon.tp4.service.WalletService;

@Service
public final class CashServiceStrategy implements ITransactionStrategy {

	private final WalletService service;
	private final ITransactionRepository repository;
	
	@Autowired
	public CashServiceStrategy(WalletService service, ITransactionRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	
	private Transaction deposit(Wallet wallet, Transaction deposit) throws UserNotFoundException, WalletNotFoundException, TransactionException {
		deposit.setWallet(wallet);
		try {
			wallet.deposit(deposit);
			var savedDeposit = repository.save(deposit);
			service.save(wallet);
			return savedDeposit;
		} catch (TransactionException e) {
			throw e;
		} 
	}
	
	private void withdraw(Wallet wallet, double amount) {
	}

	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return deposit(wallet, transaction);
	}

}
