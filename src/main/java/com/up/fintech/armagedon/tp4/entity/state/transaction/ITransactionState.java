package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

public interface ITransactionState {

	public void changeState();
	public TransactionStatusEnum getState();
	public void cancel();
	public void reject();
	public Transaction execute(Wallet wallet); //, Transaction transactio 
}
