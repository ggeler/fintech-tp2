package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class InvalidState extends AbstractTransactionState {

	public InvalidState(Transaction transaction) throws TransactionException {
		super(transaction);
		log.error(String.format("Entrando en estádo invalidado - %s %s %s", transaction.getTransactionId(), transaction.getStatus(), transaction.getType()));
		var msg = String.format("Entrando en estádo invalidado - %s %s %s", transaction.getTransactionId(), transaction.getStatus(), transaction.getType());
		new TransactionException(msg).printStackTrace();
		throw new TransactionException(msg);
	}

	@Override
	public void changeState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TransactionStatusEnum getState() {
		return TransactionStatusEnum.INVALIDSTATE;
	}

	@Override
	public Transaction execute(Wallet wallet) {
		throw new TransactionException("Error - No se puede ejecutar una transacción sobre una transacción en estado Invalido");
	}

}
