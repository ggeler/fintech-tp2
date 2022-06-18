package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.credit.ExternalIn;
import com.up.fintech.armagedon.tp4.misc.error.CvuException;
import com.up.fintech.armagedon.tp4.misc.error.ExternalBankException;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.repository.IWalletRepository;
import com.up.fintech.armagedon.tp4.service.ExternalBankService;

@Service
public final class BORRARExternalReceiveTransferServiceStrategy {//implements ITransactionStrategy {

	private final IWalletRepository repository;
	private final ExternalBankService bankService;
	
	public BORRARExternalReceiveTransferServiceStrategy(IWalletRepository repository, ExternalBankService bankService) {
		this.repository = repository;
		this.bankService = bankService;
	}
	
	private ExternalIn directDeposit(Wallet wallet, ExternalIn transfer) throws CvuException, ExternalBankException, TransactionException {
//		var externalBank = bankService.getExternalBank(transfer.getFromCvu());
//		//externalTransfer.setStatus(TransactionStatus.RECEIVING);
//		transfer.setExternalBank(externalBank);
//		transfer.setWalletInformation(wallet.getUser().getEmail());
//		wallet.directDeposit(transfer);
//		transfer.setWallet(wallet);
//		repository.save(wallet);
//		return transfer;
		return null;
	}

//	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return directDeposit(wallet, (ExternalIn) transaction);
	}

}
