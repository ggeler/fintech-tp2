package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public class PendingConfirmationState extends AbstractTransactionState {

	public PendingConfirmationState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void changeState() {
		var type = transaction.getType();
		AbstractTransactionState newState = null;
		switch (type) {
			case EXTERNAL_RECEIVE_WITHCONFIRM:
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
	public TransactionStatusEnum getState() {
		return TransactionStatusEnum.PENDING_CONFIRMATION;
	}

}
