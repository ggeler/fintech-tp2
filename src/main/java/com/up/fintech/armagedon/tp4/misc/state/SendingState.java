package com.up.fintech.armagedon.tp4.misc.state;

import com.up.fintech.armagedon.tp4.entity.Transaction;

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
		return TransactionStatus.SENDING;
	}

}
