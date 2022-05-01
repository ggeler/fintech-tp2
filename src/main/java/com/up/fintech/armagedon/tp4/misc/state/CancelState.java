package com.up.fintech.armagedon.tp4.misc.state;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public class CancelState extends AbstractTransactionState {

	public CancelState(Transaction transaction) {
		super(transaction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void changeState() {
		
	}

	@Override
	public TransactionStatus getState() {
		return TransactionStatus.CANCEL;
	}

}
