package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public class CancelState extends AbstractTransactionState {

	public CancelState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void changeState() {
		
	}

	@Override
	public TransactionStatusEnum getState() {
		return TransactionStatusEnum.CANCEL;
	}

}
