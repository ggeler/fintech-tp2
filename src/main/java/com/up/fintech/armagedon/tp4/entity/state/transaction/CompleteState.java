package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public class CompleteState extends AbstractTransactionState {

	public CompleteState(Transaction transaction) {
		super(transaction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void changeState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TransactionStatusEnum getState() {
		return TransactionStatusEnum.COMPLETED;
	}

}
