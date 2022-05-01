package com.up.fintech.armagedon.tp4.misc.state;

public interface ITransactionState {

	public void changeState();
	public TransactionStatus getState();
	public void cancel();
	public void reject(); 
}
