package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

@Service
public final class ExternalSendTransferServiceStrategy implements ITransactionStrategy {

	private void sendExternal() {
		
	}

	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		sendExternal();
		return transaction;
	}
}
