package com.up.fintech.armagedon.tp4.entity.credit;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.state.transaction.TransactionStatusEnum;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

public abstract class Credit extends Transaction {
	
	public double directDeposit() throws TransactionException {
		depositRequest();
		return confirmDepositRequest();
	}
	
	public void cancelDepositRequest() throws TransactionException {
		Transaction transaction = this;
		
		if (transaction.getStatus()!=TransactionStatusEnum.PENDING_CONFIRMATION)
			throw new TransactionException("Transacción/Credito debe estar pendiente de confirmación para cancelar");
		
		transaction.getState().cancel();
		transaction.setNote("Depósito Cancelado");
	}
	
	public double confirmDepositRequest() throws TransactionException {
		Transaction transaction = this;

		if (transaction.getStatus()!=TransactionStatusEnum.PENDING_CONFIRMATION)
			throw new TransactionException("Transacción/Credito debe estar pendiente de confirmación para confirmar");
		
		if (transaction.getAmount()>0) {
			transaction.getState().changeState();
			var newAmount = wallet.getBalance()+transaction.getAmount();
			transaction.setNote("Depósito Confirmado");
			return newAmount;
		} else {
			transaction.getState().reject();
			wallet.getTransactions().add(transaction);
			var note = "Rejected: amount to deposit cant be negative or zero "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
	}
	
	public void depositRequest() throws TransactionException {
		Transaction transaction = this;
		
		if (transaction.getStatus()!=TransactionStatusEnum.NEW)
			throw new TransactionException("Transacción de Crédito en proceso, no se puede volver a enviar para re-confirmar");

		if (transaction.getAmount()>0) {
			transaction.getState().changeState();
			wallet.getTransactions().add(transaction);
			transaction.getState().changeState();
			transaction.setNote("Depósito Pendiente de Confirmacion");
		} else {
			transaction.getState().reject();
			wallet.getTransactions().add(transaction);
			var note = "Rejected: amount to deposit cant be negative or zero "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
	}
}
