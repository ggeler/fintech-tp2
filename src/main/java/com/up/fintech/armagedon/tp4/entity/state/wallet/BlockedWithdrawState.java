package com.up.fintech.armagedon.tp4.entity.state.wallet;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;
import com.up.fintech.armagedon.tp4.entity.debit.ExternalOut;
import com.up.fintech.armagedon.tp4.entity.debit.InternalOut;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

public class BlockedWithdrawState extends AbstractWalletState {

	public BlockedWithdrawState(Wallet wallet) {
		super(wallet);
	}

	@Override
	public WalletStatusEnum getState() {
		return WalletStatusEnum.BLOCKED_WITHDRAW;
	}

	@Override
	public void closed() {
		var state = new ClosedState(this.wallet);
		wallet.setState(state);	
	}

	@Override
	public void blocked() {
		var state = new ClosedState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void blockWithdraw() {
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
		if (transaction instanceof InternalOut || transaction instanceof ExternalOut || transaction instanceof Withdraw)
			throw new TransactionException("No se puede Transferir/Retirar dinero sobre una cta bloqueada");
		else
			return transaction.execute(this.wallet);
	}

}
