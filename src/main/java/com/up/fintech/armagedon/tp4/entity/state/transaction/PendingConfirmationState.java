package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

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
			case WITHDRAW:
			case DEPOSIT:
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

	@Override
	public Transaction execute(Wallet wallet) {
		return this.transaction.getStrategy().execute(wallet, this.transaction);
	}

}
