package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

public class PayingWinBetState extends AbstractTransactionState {
	public PayingWinBetState(Transaction transaction)  {
		super(transaction);
	}

	@Override
	public void changeState() {
		var type = transaction.getType();
		AbstractTransactionState newState = null;
		switch (type) {
			case DEBITBET:
			case PAYBET:
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
		return TransactionStatusEnum.PAYINGWINNER;
	}

	@Override
	public Transaction execute(Wallet wallet) {
		throw new TransactionException("Error - No se puede ejecutar una transacción sobre una transacción en estado Completo");
	}
}
