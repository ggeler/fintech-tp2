package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

public class FeePayState extends AbstractTransactionState {

	public FeePayState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void changeState() {
		var type = transaction.getType();
		AbstractTransactionState newState = null;
		switch (type) {
			case FEEPAY:
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
		return TransactionStatusEnum.PAYINGFEE;
	}

	@Override
	public Transaction execute(Wallet wallet) {
//		return transaction.getStrategy().execute(wallet, this.transaction);
		return null;
	}

}
