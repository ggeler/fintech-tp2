package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.InternalReceiveTransfer;
import com.up.fintech.armagedon.tp4.entity.InternalSendTransfer;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.CvuException;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.repository.IWalletRepository;
import com.up.fintech.armagedon.tp4.service.WalletService;

@Service
public final class InternalSendTransferServiceStrategy implements ITransactionStrategy {

	private final IWalletRepository repository;
	private final WalletService service;
	
	public InternalSendTransferServiceStrategy(IWalletRepository repository, WalletService service) {
		this.repository = repository;
		this.service = service;
	}
	
	private InternalSendTransfer internalTransfer(Wallet fromWallet, InternalSendTransfer transfer) throws UserNotFoundException, WalletNotFoundException, TransactionException, CvuException {
		Wallet toWallet;
		if (transfer.getAmount()<=0) 
			throw new TransactionException("Amount to Transfer must be > 0");
		try {
			toWallet = transfer.getToWallet() != null ? service.getWallet(transfer.getToWallet()) : service.getWallet(transfer.getToCvu());
			if (toWallet == null) 
				throw new TransactionException("Target wallet not found by CVU/WalletId");
		} catch (WalletNotFoundException | TransactionException | UserNotFoundException | CvuException e) {
			throw new TransactionException("Destination account not found | " + e.getMessage());
		}
		try {
			transfer.setWallet(fromWallet);
			var receiveTransfer = new InternalReceiveTransfer(transfer,toWallet);
			fromWallet.debit(transfer);
			toWallet.execute(receiveTransfer);
			
			transfer.setAmount(transfer.getAmount()*-1);
			repository.save(fromWallet);
			return transfer;
		} catch (TransactionException e) {
			throw e;
		} 
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		if (transaction instanceof InternalSendTransfer) {
				return internalTransfer(wallet, (InternalSendTransfer) transaction);
		} else {
			throw new TransactionException("Error: tipo de objeto no es de transferencia");
		}
	}
}
