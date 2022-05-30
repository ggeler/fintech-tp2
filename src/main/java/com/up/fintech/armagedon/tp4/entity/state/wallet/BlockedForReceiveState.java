package com.up.fintech.armagedon.tp4.entity.state.wallet;

import com.up.fintech.armagedon.tp4.entity.Deposit;
import com.up.fintech.armagedon.tp4.entity.ExternalReceiveTransfer;
import com.up.fintech.armagedon.tp4.entity.InternalReceiveTransfer;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

public class BlockedForReceiveState extends AbstractWalletState {

	public BlockedForReceiveState(Wallet wallet) {
		super(wallet);
		// TODO Auto-generated constructor stub
	}

	@Override
	public WalletStatusEnum getState() {
		return WalletStatusEnum.BLOCKED_RECEIVE;
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
	}

	@Override
	public void enabled() {
		var state = new EnabledState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public Transaction executeTransaction(Transaction transaction) {
		if (transaction instanceof InternalReceiveTransfer || transaction instanceof ExternalReceiveTransfer || transaction instanceof Deposit)
			throw new TransactionException("No se puede Despositar/Recibir dinero sobre una cta bloqueada");
		else
			return transaction.execute(this.wallet);
	}

}
