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
public final class ExternalReceiveTransferConfirmationServiceStrategy implements ITransactionStrategy {

	private final ITransactionRepository transactionRepository;
	
	public ExternalReceiveTransferConfirmationServiceStrategy(ITransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	
	private ExternalReceiveTransfer confirmExternalTransferReceive(Wallet wallet, ExternalReceiveTransfer transfer) throws CvuException, ExternalBankException, TransactionException {
		wallet.confirmDeposit(transfer);
		transfer.setWalletInformation(wallet.getUser().getEmail());
		return transactionRepository.save(transfer);
	}

	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return confirmExternalTransferReceive(wallet, (ExternalReceiveTransfer) transaction);
	}

}
