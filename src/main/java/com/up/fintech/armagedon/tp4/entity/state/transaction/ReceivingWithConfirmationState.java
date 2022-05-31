package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public class ReceivingWithConfirmationState extends AbstractTransactionState {

	public ReceivingWithConfirmationState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void changeState() {
		var type = transaction.getType();
		AbstractTransactionState newState = null;
		switch (type) {
			case EXTERNAL_RECEIVE_WITHCONFIRM:
			case INTERNAL_RECEIVE:
				newState = new PendingConfirmationState(transaction);
				break;
			default:
				newState = new InvalidState(transaction);
				break;
		}
		transaction.setState(newState);
	}

	@Override
	public TransactionStatusEnum getState() {
		return TransactionStatusEnum.RECEIVING_WITH_CONFIRMATION;
	}

}
