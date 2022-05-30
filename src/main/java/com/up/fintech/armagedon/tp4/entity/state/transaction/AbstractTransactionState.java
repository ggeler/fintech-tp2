package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public abstract class AbstractTransactionState implements ITransactionState {

	protected Transaction transaction;
	
	public AbstractTransactionState(Transaction transaction) {
		this.transaction = transaction;
		transaction.setStatus(this.getState());
	}
	
	@Override
	public void cancel() {
		this.transaction.setState(new CancelState(transaction));
	}
	
	@Override
	public void reject() {
		this.transaction.setState(new RejectedState(transaction));
	}
		
}
