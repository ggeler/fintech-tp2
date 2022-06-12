package com.up.fintech.armagedon.tp4.entity.state.wallet;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

public class BlockedState extends AbstractWalletState {

	public BlockedState(Wallet wallet) {
		super(wallet);
		// TODO Auto-generated constructor stub
	}

	@Override
	public WalletStatusEnum getState() {
		return WalletStatusEnum.BLOCKED;
	}

	@Override
	public void closed() {
		var state = new ClosedState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void blocked() {
	}

	@Override
	public void blockWithdraw() {
		var state = new BlockedWithdrawState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void blockDeposit() {
		var state = new BlockedDepositState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void enabled() {
		var state = new EnabledState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public Transaction executeTransaction(Transaction transaction) {
		throw new TransactionException("No se puede realizar una transacción sobre una cta bloqueada");
	}

}
