package com.up.fintech.armagedon.tp4.entity.state.wallet;

import com.up.fintech.armagedon.tp4.entity.ExternalSendTransfer;
import com.up.fintech.armagedon.tp4.entity.InternalSendTransfer;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

public class BlockedForSendState extends AbstractWalletState {

	public BlockedForSendState(Wallet wallet) {
		super(wallet);
	}

	@Override
	public WalletStatusEnum getState() {
		return WalletStatusEnum.BLOCKED_SEND;
	}

	@Override
	public void closed() {
		var state = new ClosedState(this.wallet);
		wallet.setState(state);	
	}

	@Override
	public void block() {
		var state = new ClosedState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void blockForSend() {
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

	@Override
	public Transaction executeTransaction(Transaction transaction) {
		if (transaction instanceof InternalSendTransfer || transaction instanceof ExternalSendTransfer)
			throw new TransactionException("No se puede Transferir/Retirar dinero sobre una cta bloqueada");
		else
			return transaction.execute(this.wallet);
	}

}
