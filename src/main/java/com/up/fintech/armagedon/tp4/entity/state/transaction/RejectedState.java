package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

public class RejectedState extends AbstractTransactionState {

	public RejectedState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void changeState() {
		
	}

	@Override
	public TransactionStatusEnum getState() {
		return TransactionStatusEnum.REJECTED;
	}

	@Override
	public Transaction execute(Wallet wallet) {
		// TODO Auto-generated method stub
		return null;
	}

}
