package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public class NewState extends AbstractTransactionState {

	public NewState(Transaction transaction) {
		super(transaction);
	}
	
	@Override
	public void changeState() {
		var type = transaction.getType();
		AbstractTransactionState newState = null;
		switch (type) {
			case DEPOSIT:
				newState = new DepositState(transaction);
				break;
			case WITHDRAW:
				newState = new WithDrawingState(transaction);
				break;
			case EXTERNAL_SEND:
				newState = new SendingState(transaction);
				break;
			case EXTERNAL_RECEIVE:
				newState = new ReceivingState(transaction);
				break;
			case EXTERNAL_RECEIVE_WITHCONFIRM:
				newState = new ReceivingWithConfirmationState(transaction);
				break;
			case INTERNAL_RECEIVE:
				newState = new ReceivingState(transaction);
				break;
			case INTERNAL_SEND:
				newState = new SendingState(transaction);
				break;
		}
		transaction.setState(newState);
	}

	@Override
	public TransactionStatusEnum getState() {
		return TransactionStatusEnum.NEW;
	}
}
