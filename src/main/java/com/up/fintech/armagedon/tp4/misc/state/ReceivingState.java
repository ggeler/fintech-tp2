package com.up.fintech.armagedon.tp4.misc.state;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public class ReceivingState extends AbstractTransactionState {

	public ReceivingState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void changeState() {
		var type = transaction.getType();
		AbstractTransactionState newState = null;
		switch (type) {
			case EXTERNAL_RECEIVE:
			case INTERNAL_RECEIVE:
				newState = new CompleteState(transaction);
				break;
			default:
				newState = new InvalidState(transaction);
				break;
		}
		transaction.setState(newState);
	}

	@Override
	public TransactionStatus getState() {
		return TransactionStatus.RECEIVING;
	}

}