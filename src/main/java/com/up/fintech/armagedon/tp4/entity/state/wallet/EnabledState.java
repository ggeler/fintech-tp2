package com.up.fintech.armagedon.tp4.entity.state.wallet;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

public class EnabledState extends AbstractWalletState {

	public EnabledState(Wallet wallet) {
		super(wallet);
	}

	@Override
	public WalletStatusEnum getState() {
		return WalletStatusEnum.ENABLED;
	}

	@Override
	public Transaction executeTransaction(Transaction transaction) {
		return transaction.execute(this.wallet);
	}

	@Override
	public void closed() {
		var state = new ClosedState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void block() {
		var state = new BlockedState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void blockForSend() {
		var state = new BlockedForSendState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void blockForReceive() {
		var state = new BlockedForReceiveState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void enabled() {
		var state = new EnabledState(this.wallet);
		wallet.setState(state);
	}

}
