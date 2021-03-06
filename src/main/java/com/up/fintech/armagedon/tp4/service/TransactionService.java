package com.up.fintech.armagedon.tp4.service;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.google.zxing.WriterException;
import com.up.fintech.armagedon.tp4.dto.ExternalTransferDto;
import com.up.fintech.armagedon.tp4.entity.IQr;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.credit.Deposit;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;
import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.entity.debit.ExternalOut;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.repository.IBetRepository;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;
import com.up.fintech.armagedon.tp4.strategy.BetCancelServiceStrategy;
import com.up.fintech.armagedon.tp4.strategy.BetConfirmServiceStrategy;
import com.up.fintech.armagedon.tp4.strategy.DepositCancelServiceStrategy;
import com.up.fintech.armagedon.tp4.strategy.DepositConfirmServiceStrategy;
import com.up.fintech.armagedon.tp4.strategy.ExternalReceiveTransferCancelServiceStrategy;
import com.up.fintech.armagedon.tp4.strategy.ExternalReceiveTransferConfirmationServiceStrategy;
import com.up.fintech.armagedon.tp4.strategy.QrServiceStrategy;
import com.up.fintech.armagedon.tp4.strategy.WithdrawCancelServiceStrategy;
import com.up.fintech.armagedon.tp4.strategy.WithdrawConfirmationServiceStrategy;

@Service
public class TransactionService {

	private final WalletService service;
	private final ITransactionRepository repository;
	
	@Autowired
	public TransactionService(WalletService service, ITransactionRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	
	public Transaction save(Transaction transaction) {
		return repository.save(transaction);
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

	public Transaction getTransaction(UUID walletId, UUID transactionId) {
		var transaction = getTransaction(transactionId);
		var wallet = service.getWallet(walletId);
		if (transaction.getWallet() != wallet)
			throw new TransactionException("Transaccion no pertenece a esta wallet");
		return transaction;
	}
	
	private Transaction getTransaction(UUID transactionId) throws TransactionException {
		var transaction = repository.findByTransactionId(transactionId).orElseThrow(() -> new TransactionException("Transaction not found"));
		transaction.setTransactionState();
		return transaction;
	}
	
	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.REPEATABLE_READ)
	public Transaction confirm(UUID walletId, UUID transactionId, ExternalTransferDto confirmation) throws TransactionException {
		var transaction = getTransaction(transactionId);
		var wallet = service.getWallet(walletId);
		
		if (transaction.getWallet()!=wallet)
			throw new TransactionException("Transaccion no pertenece a esta wallet");
		
		if (transaction instanceof ExternalOut tmp ) { 
			if (!tmp.getConfirmationCode().equals(confirmation.getConfirmation()))
					throw new TransactionException("C??digo de confirmaci??n invalido"); 
			transaction.setStrategy(SpringContext.getBean(ExternalReceiveTransferConfirmationServiceStrategy.class));
		}
		else if (transaction instanceof Withdraw tmp) {
			if (!tmp.getConfirmationCode().equals(confirmation.getConfirmation()))
				throw new TransactionException("C??digo de confirmaci??n invalido"); 
			transaction.setStrategy(SpringContext.getBean(WithdrawConfirmationServiceStrategy.class));
		}
		else if (transaction instanceof Deposit tmp) {
			if (!tmp.getConfirmationCode().equals(confirmation.getConfirmation()))
				throw new TransactionException("C??digo de confirmaci??n invalido"); 
			transaction.setStrategy(SpringContext.getBean(DepositConfirmServiceStrategy.class));
		} else if (transaction instanceof Bet tmp) {
			if (!tmp.getConfirmationCode().equals(confirmation.getConfirmation()))
				throw new TransactionException("C??digo de confirmaci??n invalido"); 
			transaction.setStrategy(SpringContext.getBean(BetConfirmServiceStrategy.class));
		}
		return wallet.execute(transaction);
	}

	@Transactional(label = "WalletTransactionConfirmation", isolation = Isolation.REPEATABLE_READ)
	public Transaction cancel(UUID walletId, UUID transactionId, ExternalTransferDto confirmation) {
		var transaction = getTransaction(transactionId);
		var wallet = service.getWallet(walletId);
		
		if (transaction.getWallet()!=wallet)
			throw new TransactionException("Transaccion no pertenece a esta wallet");
		
		if (transaction instanceof ExternalOut tmp) {
			if (!tmp.getConfirmationCode().equals(confirmation.getConfirmation()))
				throw new TransactionException("C??digo de confirmaci??n invalido");
			transaction.setStrategy(SpringContext.getBean(ExternalReceiveTransferCancelServiceStrategy.class));
		} else if (transaction instanceof Withdraw tmp) {
			if (!tmp.getConfirmationCode().equals(confirmation.getConfirmation()))
				throw new TransactionException("C??digo de confirmaci??n invalido");
			transaction.setStrategy(SpringContext.getBean(WithdrawCancelServiceStrategy.class));
		} else if (transaction instanceof Deposit tmp) {
			if (!tmp.getConfirmationCode().equals(confirmation.getConfirmation()))
				throw new TransactionException("C??digo de confirmaci??n invalido");
			transaction.setStrategy(SpringContext.getBean(DepositCancelServiceStrategy.class));
		} else if (transaction instanceof Bet tmp) {
			if (!tmp.getConfirmationCode().equals(confirmation.getConfirmation()))
				throw new TransactionException("C??digo de confirmaci??n invalido");
			transaction.setStrategy(SpringContext.getBean(BetCancelServiceStrategy.class));
		}
		
		return wallet.execute(transaction);
	}

	public BufferedImage getQr(UUID walletId, UUID transactionId) throws WriterException {
		
		var transaction = getTransaction(transactionId);
		var wallet = service.getWallet(walletId);
		if (transaction instanceof IQr)
		{
			BufferedImage qr = null;
			transaction.setStrategy(SpringContext.getBean(QrServiceStrategy.class));
			qr = ((IQr) wallet.execute(transaction)).getQr();
			return qr;	
		} else
			throw new TransactionException("Debe ser deposito o retiro para obtener QR");
//		if (transaction instanceof Deposit || transaction instanceof Withdraw )
//		{
//			BufferedImage qr = null;
//			transaction.setStrategy(SpringContext.getBean(QrServiceStrategy.class));
//			if (transaction instanceof Deposit) { 
//				qr = ((Deposit) wallet.execute(transaction)).getQr();
//			} else if (transaction instanceof Withdraw) {
//				qr = ((Withdraw) wallet.execute(transaction)).getQr();
//			}
//			return qr;	
//		} else
//			throw new TransactionException("Debe ser deposito o retiro para obtener QR");
	}
}
