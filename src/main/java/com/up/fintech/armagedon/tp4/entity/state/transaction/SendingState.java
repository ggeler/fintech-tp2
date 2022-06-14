package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

public class SendingState extends AbstractTransactionState {

	public SendingState(Transaction transaction) {
		super(transaction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void changeState() {
		var type = transaction.getType();
		AbstractTransactionState newState = null;
		switch (type) {
			case EXTERNAL_SEND:
			case INTERNAL_SEND:
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
		return TransactionStatusEnum.SENDING;
	}

	@Override
	public Transaction execute(Wallet wallet) {
		// TODO Auto-generated method stub
		return null;
	}

}
