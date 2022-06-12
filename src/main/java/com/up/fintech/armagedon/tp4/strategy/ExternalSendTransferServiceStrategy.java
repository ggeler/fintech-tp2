package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.ExternalOut;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.service.ExternalBankService;

@Service
public final class ExternalSendTransferServiceStrategy implements ITransactionStrategy {

	private final ExternalBankService service;
	
	public ExternalSendTransferServiceStrategy(ExternalBankService service) {
		this.service = service;
	}
	
	private void sendExternal() {
		
	}

	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		if (transaction instanceof ExternalOut tmp) {
			service.isExternalValid(tmp.getToCvu());	
			sendExternal();
			return transaction;
		} else {
			throw new TransactionException("Error: tipo de objeto no es de transferencia");
		}
	}
}
