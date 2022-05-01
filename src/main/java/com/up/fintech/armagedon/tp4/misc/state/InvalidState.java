package com.up.fintech.armagedon.tp4.misc.state;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public class InvalidState extends AbstractTransactionState {

	public InvalidState(Transaction transaction) {
		super(transaction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void changeState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TransactionStatus getState() {
		// TODO Auto-generated method stub
		return null;
	}

}
