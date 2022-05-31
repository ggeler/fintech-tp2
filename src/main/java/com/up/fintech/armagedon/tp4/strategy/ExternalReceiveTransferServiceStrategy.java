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
	private final ExternalBankService bankService;
	
	public ExternalReceiveTransferServiceStrategy(IWalletRepository repository, ExternalBankService bankService) {
		this.repository = repository;
		this.bankService = bankService;
	}
	
	private ExternalReceiveTransfer externalTransferReceive(Wallet wallet, ExternalReceiveTransfer transfer) throws CvuException, ExternalBankException, TransactionException {
		var externalBank = bankService.getExternalBank(transfer.getFromCvu());
		//externalTransfer.setStatus(TransactionStatus.RECEIVING);
		transfer.setExternalBank(externalBank);
		transfer.setWalletInformation(wallet.getUser().getEmail());
		wallet.deposit(transfer);
		transfer.setWallet(wallet);
		repository.save(wallet);
		return transfer;
	}

	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return externalTransferReceive(wallet, (ExternalReceiveTransfer) transaction);
	}

}
