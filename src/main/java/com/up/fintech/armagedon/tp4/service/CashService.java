package com.up.fintech.armagedon.tp4.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Deposit;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;

@Service
public class CashService {

	private final WalletService service;
	private final ITransactionRepository repository;
	
	@Autowired
	public CashService(WalletService service, ITransactionRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	
	public Deposit deposit(UUID uuid, Deposit deposit) throws UserNotFoundException, WalletNotFoundException, TransactionException {
		Deposit savedDeposit;
		var wallet = service.getWalletByWalletId(uuid);
		deposit.setWallet(wallet);
		try {
			wallet.deposit(deposit);
		} catch (TransactionException e) {
			throw e;
		} finally {
			savedDeposit = repository.save(deposit);
			service.updateWallet(wallet);
		}
		return savedDeposit;
	}
	
	public void withdraw(Wallet wallet, double amount) {
	}
}
