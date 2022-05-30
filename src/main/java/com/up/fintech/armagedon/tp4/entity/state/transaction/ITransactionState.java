package com.up.fintech.armagedon.tp4.entity.state.transaction;

public interface ITransactionState {

	public void changeState();
	public TransactionStatusEnum getState();
	public void cancel();
	public void reject(); 
}
