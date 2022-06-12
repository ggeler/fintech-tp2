package com.up.fintech.armagedon.tp4.entity.state.wallet;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

public interface IWalletState {
	
	public void closed();
	public void blocked();
	public void blockWithdraw();
	public void blockDeposit();
	public void enabled();
	public WalletStatusEnum getState();
	public Transaction executeTransaction(Transaction transaction) throws TransactionException;
	
}
