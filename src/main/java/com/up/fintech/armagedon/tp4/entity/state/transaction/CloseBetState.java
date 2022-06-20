package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

public class CloseBetState extends AbstractTransactionState {
	public CloseBetState(Transaction transaction)  {
		super(transaction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void changeState() {
		
	}

	@Override
	public TransactionStatusEnum getState() {
		return TransactionStatusEnum.CLOSED;
	}

	@Override
	public Transaction execute(Wallet wallet) {
		throw new TransactionException("Error - No se puede ejecutar una transacción sobre una transacción en estado Completo");
	}
}
