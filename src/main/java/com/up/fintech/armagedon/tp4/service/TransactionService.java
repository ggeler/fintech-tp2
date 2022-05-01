package com.up.fintech.armagedon.tp4.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;

@Service
public class TransactionService {

	private final WalletService service;
	private final ITransactionRepository repository;
	
	@Autowired
	public TransactionService(WalletService service, ITransactionRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	public List<Transaction> getTransactions(UUID uuid) throws UserNotFoundException, WalletNotFoundException {
		var wallet = service.getWalletByWalletId(uuid);
		var transactions = wallet.getTransactions();
		return transactions;
	}
	
	public Page<Transaction> getTransactions(UUID uuid, Pageable pageable) throws UserNotFoundException, WalletNotFoundException {
		var wallet = service.getWalletByWalletId(uuid);
		var transactions = repository.findAllByWallet(wallet,pageable);
		return transactions;
	}
}
