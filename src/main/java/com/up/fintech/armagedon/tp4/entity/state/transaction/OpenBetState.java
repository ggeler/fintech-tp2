package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

public class OpenBetState extends AbstractTransactionState {
	public OpenBetState(Transaction transaction)  {
		super(transaction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void changeState() {
		var type = transaction.getType();
		AbstractTransactionState newState = null;
		switch (type) {
			default:
				newState = new InvalidState(transaction);
				break;
		}
		transaction.setState(newState);
	}

	public void win() {
		if (transaction.getType()==TransactionType.BET) {
			transaction.setState(new WinBetState(transaction));
		}
	}
	public void lose() {
		if (transaction.getType()==TransactionType.BET) {
			transaction.setState(new LoseBetState(transaction));
		}
	}
	@Override
	public TransactionStatusEnum getState() {
		return TransactionStatusEnum.OPEN;
	}

	@Override
	public Transaction execute(Wallet wallet) {
		throw new TransactionException("Error - No se puede ejecutar una transacción sobre una transacción en estado Completo");
	}
}
