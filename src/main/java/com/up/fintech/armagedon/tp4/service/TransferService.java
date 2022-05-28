package com.up.fintech.armagedon.tp4.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.ExternalReceiveTransfer;
import com.up.fintech.armagedon.tp4.entity.InternalReceiveTransfer;
import com.up.fintech.armagedon.tp4.entity.InternalSendTransfer;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.CvuException;
import com.up.fintech.armagedon.tp4.misc.error.ExternalBankException;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.misc.state.TransactionStatus;
import com.up.fintech.armagedon.tp4.repository.IWalletRepository;

@Service
public class TransferService  {

	private final IWalletRepository repository;
	private final WalletService service;
	private final ExternalBankService bankService;
	
	public TransferService(IWalletRepository repository, WalletService service, ExternalBankService bankService) {
		this.repository = repository;
		this.service = service;
		this.bankService = bankService;
	}
	
	public InternalSendTransfer internalTransfer(UUID uuid, InternalSendTransfer transfer) throws UserNotFoundException, WalletNotFoundException, TransactionException, CvuException {
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
			
			var receiveTransfer = new InternalReceiveTransfer();
			receiveTransfer.setAmount(transfer.getAmount());
			receiveTransfer.setFromWallet(uuid);
			receiveTransfer.setTransactionId(transfer.getTransactionId());
			receiveTransfer.setWallet(targetWallet);
			
			transfer.setAmount(transfer.getAmount()*-1);
			originWallet.debit(transfer);
			targetWallet.deposit(receiveTransfer);
			
		} catch (TransactionException e) {
			throw e;
		} finally {
			repository.save(originWallet);
			repository.save(targetWallet);
		}
		return transfer;
	}

	public ExternalReceiveTransfer externalTransferReceive(ExternalReceiveTransfer externalTransfer) throws CvuException, ExternalBankException, TransactionException {
		var destinationWallet = service.getWalletByCvu(externalTransfer.getToCvu());
		var externalBank = bankService.getExternalBank(externalTransfer.getFromCvu());
		//externalTransfer.setStatus(TransactionStatus.RECEIVING);
		externalTransfer.setExternalBank(externalBank);
		destinationWallet.deposit(externalTransfer);
		externalTransfer.setWallet(destinationWallet);
		repository.save(destinationWallet);
		return externalTransfer;
	}

}
