package com.up.fintech.armagedon.tp4.misc.state;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public class RejectedState extends AbstractTransactionState {

	public RejectedState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void changeState() {
		
	}

	@Override
	public TransactionStatus getState() {
		return TransactionStatus.REJECTED;
	}

}
