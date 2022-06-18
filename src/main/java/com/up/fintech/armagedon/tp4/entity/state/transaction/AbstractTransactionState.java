package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;

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
	
	@Override
	public void preview() {
		if (transaction instanceof Withdraw)
			this.transaction.setState(new PreviewState(transaction));
	}
}
