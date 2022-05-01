package com.up.fintech.armagedon.tp4.misc.state;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public class NewState extends AbstractTransactionState {

	public NewState(Transaction transaction) {
		super(transaction);
//		switch (transaction.getStatus()) {
//		case CANCEL:
//			transaction.setState(new CancelState(transaction));
//			break;
//		case COMPLETED:
//			transaction.setState(new CompleteState(transaction));
//			break;
//		case DEPOSITING:
//			transaction.setState(new DepositState(transaction));
//			break;
//		case RECEIVING:
//			transaction.setState(new ReceivingState(transaction));
//			break;
//		case REJECTED:
//			transaction.setState(new RejectedState(transaction));
//			break;
//		case SENDING:
//			transaction.setState(new SendingState(transaction));
//			break;
//		case WITHDRAWING:
//			transaction.setState(new WithDrawingState(transaction));
//			break;
//		case NEW:
//			break;
//		}
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
	public TransactionStatus getState() {
		return TransactionStatus.NEW;
	}
}
