package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.ExternalReceiveTransfer;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.CvuException;
import com.up.fintech.armagedon.tp4.misc.error.ExternalBankException;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;
import com.up.fintech.armagedon.tp4.service.ExternalBankService;

@Service
public final class ExternalReceiveTransferWithConfirmationServiceStrategy implements ITransactionStrategy {

	private final ITransactionRepository transactionRepository;
	private final ExternalBankService bankService;
	
	public ExternalReceiveTransferWithConfirmationServiceStrategy(ExternalBankService bankService, ITransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
		this.bankService = bankService;
	}
	
	private ExternalReceiveTransfer externalTransferReceive(Wallet wallet, ExternalReceiveTransfer transfer) throws CvuException, ExternalBankException, TransactionException {
		var externalBank = bankService.getExternalBank(transfer.getFromCvu());
		transfer.setType(TransactionType.EXTERNAL_RECEIVE_WITHCONFIRM);
		transfer.setExternalBank(externalBank);
		wallet.receiveDeposit(transfer);
		transfer.setWallet(wallet);
		transfer.setWalletInformation(wallet.getUser().getEmail());
		return transactionRepository.save(transfer);
	}

	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return externalTransferReceive(wallet, (ExternalReceiveTransfer) transaction);
	}

}
