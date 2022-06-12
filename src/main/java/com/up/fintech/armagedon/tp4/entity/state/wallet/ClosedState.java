package com.up.fintech.armagedon.tp4.entity.state.wallet;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

public class ClosedState extends AbstractWalletState {

	public ClosedState(Wallet wallet) {
		super(wallet);
	}

	@Override
	public WalletStatusEnum getState() {
		return WalletStatusEnum.CLOSED;
	}

	@Override
	public void closed() {
	}

	@Override
	public Transaction executeTransaction(Transaction transaction) {
		return null;
	}

	@Override
	public void blocked() {
	}

	@Override
	public void blockWithdraw() {
	}

	@Override
	public void blockDeposit() {
	}

	@Override
	public void enabled() {
	}

}
