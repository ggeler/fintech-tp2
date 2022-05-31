package com.up.fintech.armagedon.tp4.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.up.fintech.armagedon.tp4.dto.ExternalTransferDto;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;
import com.up.fintech.armagedon.tp4.strategy.ExternalReceiveTransferCancelServiceStrategy;
import com.up.fintech.armagedon.tp4.strategy.ExternalReceiveTransferConfirmationServiceStrategy;

@Service
public class TransactionService {

	private final WalletService service;
	private final ITransactionRepository repository;
	
	@Autowired
	public TransactionService(WalletService service, ITransactionRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	
	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.DEFAULT)
	public List<Transaction> getTransactions(UUID uuid) throws UserNotFoundException, WalletNotFoundException {
		var wallet = service.getWallet(uuid);
		var transactions = wallet.getTransactions();
		return transactions;
	}
	
	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.DEFAULT)
	public Page<Transaction> getTransactions(UUID uuid, Pageable pageable) throws UserNotFoundException, WalletNotFoundException {
		var wallet = service.getWallet(uuid);
		var transactions = repository.findAllByWallet(wallet,pageable);
		return transactions;
	}
	
	private Transaction getTransaction(UUID transactionId) throws TransactionException {
		var transaction = repository.findByTransactionId(transactionId).orElseThrow(() -> new TransactionException("Transaction not found"));
		transaction.setTransactionState();
		return transaction;
	}
	
	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.REPEATABLE_READ)
	public Transaction confirm(UUID walletId, UUID transactionId, ExternalTransferDto confirmation) {
		var transaction = getTransaction(transactionId);
		transaction.setStrategy(SpringContext.getBean(ExternalReceiveTransferConfirmationServiceStrategy.class));
		return service.getWallet(walletId).execute(transaction);
	}

	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.REPEATABLE_READ)
	public Transaction cancel(UUID walletId, UUID transactionId, ExternalTransferDto confirmation) {
		var transaction = getTransaction(transactionId);
		transaction.setStrategy(SpringContext.getBean(ExternalReceiveTransferCancelServiceStrategy.class));
		return service.getWallet(walletId).execute(transaction);
	}
}
