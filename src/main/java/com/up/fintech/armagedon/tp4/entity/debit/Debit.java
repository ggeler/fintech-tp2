package com.up.fintech.armagedon.tp4.entity.debit;

import java.time.Instant;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.state.transaction.TransactionStatusEnum;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

public abstract class Debit extends Transaction {
	
	public double directWithdraw() throws TransactionException {
		withdrawRequest();
		return confirmWithdrawRequest();
	}
	
	public void withdrawRequest() throws TransactionException {
		var transaction = this;
		
		if (transaction.getStatus()!=TransactionStatusEnum.NEW)
			throw new TransactionException("Transaccion de Debito en proceso, no se puede volver a enviar para re-confirmar");
		
		if (transaction.getAmount()>0 && wallet.getBalance()-transaction.getAmount()>=0) {
			transaction.getState().changeState();
			wallet.getTransactions().add(transaction);
			transaction.getState().changeState();
			transaction.setNote("Retiro Pendiente de Confirmacion");
		} else {
			transaction.getState().reject();
			wallet.getTransactions().add(transaction);
			var note = "Rejected: balance less than requested transaction or zero/negative amount requested "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
	}
	
	public double confirmWithdrawRequest() throws TransactionException {
		Transaction transaction = this;
		
		if (transaction.getStatus()!=TransactionStatusEnum.PENDING_CONFIRMATION)
			throw new TransactionException("Transacción/Débito debe estar pendiente de confirmación para confirmar");
		
		if (transaction.getAmount()>0 && wallet.getBalance()-transaction.getAmount() >= 0) {
			transaction.getState().changeState();
			var newAmount = wallet.getBalance()-transaction.getAmount();
			transaction.setConfirmedTime(Instant.now());
			transaction.setNote("Retiro Confirmado");
			return newAmount;
		} else {
			transaction.getState().reject();
			wallet.getTransactions().add(transaction);
			var note = "Rejected: amount to deposit cant be negative or zero "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
	}
	
	public void cancelWithdrawRequest() {
		Transaction transaction = this;
		if (transaction.getStatus()!=TransactionStatusEnum.PENDING_CONFIRMATION)
			throw new TransactionException("Transacción/Débito debe estar pendiente de confirmación para cancelar");
		transaction.getState().cancel();
		transaction.setCanceledTime(Instant.now());
		transaction.setNote("Retiro Cancelado");
	}
	
}
