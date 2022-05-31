package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.ExternalReceiveTransfer;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.CvuException;
import com.up.fintech.armagedon.tp4.misc.error.ExternalBankException;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;

@Service
public final class ExternalReceiveTransferCancelServiceStrategy implements ITransactionStrategy {

	private final ITransactionRepository transactionRepository;
	
	public ExternalReceiveTransferCancelServiceStrategy(ITransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	
	private ExternalReceiveTransfer cancelExternalTransferReceive(Wallet wallet, ExternalReceiveTransfer transfer) throws CvuException, ExternalBankException, TransactionException {
		wallet.cancelDeposit(transfer);
		transfer.setWalletInformation(wallet.getUser().getEmail());
		return transactionRepository.save(transfer);
	}

	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return cancelExternalTransferReceive(wallet, (ExternalReceiveTransfer) transaction);
	}

}
