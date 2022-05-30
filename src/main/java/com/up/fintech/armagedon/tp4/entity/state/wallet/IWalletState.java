package com.up.fintech.armagedon.tp4.entity.state.wallet;

import com.up.fintech.armagedon.tp4.entity.Transaction;

public interface IWalletState {
	
	public void closed();
	public void block();
	public void blockForSend();
	public void blockForReceive();
	public void enabled();
	public WalletStatusEnum getState();
	public Transaction executeTransaction(Transaction transaction);
	
}
