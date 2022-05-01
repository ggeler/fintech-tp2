package com.up.fintech.armagedon.tp4.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.InternalReceiveTransfer;
import com.up.fintech.armagedon.tp4.entity.InternalSendTransfer;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.CvuException;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.repository.IWalletRepository;

@Service
public class TransferService {

	private final IWalletRepository repository;
	private final WalletService service;
	
	public TransferService(IWalletRepository repository, WalletService service) {
		this.repository = repository;
		this.service = service;
	}
	
	public InternalSendTransfer transfer(UUID uuid, InternalSendTransfer transfer) throws UserNotFoundException, WalletNotFoundException, TransactionException, CvuException {
		var originWallet = service.getWalletByWalletId(uuid);
		Wallet targetWallet;
		try {
			if (transfer.getToWallet()!=null) {
				targetWallet = service.getWalletByWalletId(transfer.getToWallet());
			} else if (transfer.getToCvu()!=null){
				targetWallet = service.getWalletByCvu(transfer.getToCvu());
			} else {
				throw new TransactionException("Target wallet not found by CVU/WalletId");
			}
		} catch (WalletNotFoundException | TransactionException | UserNotFoundException | CvuException e) {
			throw new TransactionException("Destination account not found | " + e.getMessage());
		}
		try {
			transfer.setWallet(originWallet);
			originWallet.debit(transfer);
			var receiveTransfer = new InternalReceiveTransfer();
			receiveTransfer.setAmount(transfer.getAmount());
			receiveTransfer.setFromWallet(uuid);
			receiveTransfer.setTransactionId(transfer.getTransactionId());
			receiveTransfer.setWallet(targetWallet);
			targetWallet.deposit(receiveTransfer);
		} catch (TransactionException e) {
			throw e;
		} finally {
			repository.save(originWallet);
			repository.save(targetWallet);
		}
		return transfer;
	}
}
