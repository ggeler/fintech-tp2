package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.ExternalReceiveTransfer;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.CvuException;
import com.up.fintech.armagedon.tp4.misc.error.ExternalBankException;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.repository.IWalletRepository;
import com.up.fintech.armagedon.tp4.service.ExternalBankService;

@Service
public final class ExternalReceiveTransferServiceStrategy implements ITransactionStrategy {

	private final IWalletRepository repository;
//	private final WalletService service;
	private final ExternalBankService bankService;
	
	public ExternalReceiveTransferServiceStrategy(IWalletRepository repository, ExternalBankService bankService) {
		this.repository = repository;
//		this.service = service;
		this.bankService = bankService;
	}
	
	private ExternalReceiveTransfer externalTransferReceive(Wallet wallet, ExternalReceiveTransfer externalTransfer) throws CvuException, ExternalBankException, TransactionException {
		var destinationWallet = wallet;
		var externalBank = bankService.getExternalBank(externalTransfer.getFromCvu());
		//externalTransfer.setStatus(TransactionStatus.RECEIVING);
		externalTransfer.setExternalBank(externalBank);
		destinationWallet.deposit(externalTransfer);
		externalTransfer.setWallet(destinationWallet);
		repository.save(destinationWallet);
		return externalTransfer;
	}

	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return externalTransferReceive(wallet, (ExternalReceiveTransfer) transaction);
	}

}
