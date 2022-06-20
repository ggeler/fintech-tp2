package com.up.fintech.armagedon.tp4.entity.debit;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Entity;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.state.transaction.TransactionStatusEnum;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public abstract class Debit extends Transaction {
	
	public BigDecimal directWithdraw() throws TransactionException {
		withdrawRequest();
		return confirmWithdrawRequest();
	}
	
	public void withdrawRequest() throws TransactionException {
		var transaction = this;
		
		if (transaction.getStatus()!=TransactionStatusEnum.NEW && transaction.getStatus()!=TransactionStatusEnum.PREVIEW)
			throw new TransactionException("Transaccion de Debito en proceso, no se puede volver a enviar para re-confirmar");
		if (transaction.getAmount().compareTo(BigDecimal.ZERO)<=0) {
			transaction.getState().reject();
			var note = "Rejected: amount to withdraw cant be negative or zero "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
		//if (wallet.getBalance().compareTo(transaction.getAmount()) < 0) {
		if (wallet.getBalance().compareTo(transaction.getTotal()) < 0) {
			transaction.getState().reject();
			var note = String.format("Request Rejected: amount %f to withdraw set balance to negative - Balance %f - Transaction type: %s",
					transaction.getAmount(), wallet.getBalance(), transaction.getType().value);
			transaction.setNote(note);
			throw new TransactionException(note);
		}
		transaction.getState().changeState();
		wallet.getTransactions().add(transaction);
		transaction.getState().changeState();
		transaction.setNote(transaction.getType().value+" "+transaction.getStatus().status);
	}
	
	public BigDecimal confirmWithdrawRequest() throws TransactionException {
		Transaction transaction = this;
		
		if (transaction.getStatus()!=TransactionStatusEnum.PENDING_CONFIRMATION)
			throw new TransactionException("Transacción/Débito debe estar pendiente de confirmación para confirmar");
		
		if (transaction.getAmount().compareTo(BigDecimal.ZERO)<=0) {
			transaction.getState().reject();
			var note = "Rejected: amount to withdraw cant be negative or zero "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
//		if (wallet.getBalance().compareTo(transaction.getAmount()) < 0) {
		if (wallet.getBalance().compareTo(transaction.getTotal()) < 0) {
			transaction.getState().reject();
			var note = String.format("Confirmation Rejected: amount %f to withdraw set balance to negative - Balance %f - Transaction type: %s",
					transaction.getAmount(), wallet.getBalance(), transaction.getType().value);
			transaction.setNote(note);
			throw new TransactionException(note);
		}
		transaction.getState().changeState();
		var newAmount = wallet.getBalance().subtract(transaction.getAmount());
		transaction.setConfirmedTime(Instant.now());
		transaction.setNote(transaction.getType().value+" "+transaction.getStatus().status);
		return newAmount;
	}
	
	public void cancelWithdrawRequest() {
		Transaction transaction = this;
		if (transaction.getStatus()!=TransactionStatusEnum.PENDING_CONFIRMATION)
			throw new TransactionException("Transacción/Débito debe estar pendiente de confirmación para cancelar");
		transaction.getState().cancel();
		transaction.setCanceledTime(Instant.now());
		transaction.setNote(transaction.getType().value+" "+transaction.getStatus().status);
	}
	
}
