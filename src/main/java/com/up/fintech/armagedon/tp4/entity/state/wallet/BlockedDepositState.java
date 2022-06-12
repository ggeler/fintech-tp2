package com.up.fintech.armagedon.tp4.entity.state.wallet;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.credit.Deposit;
import com.up.fintech.armagedon.tp4.entity.credit.ExternalIn;
import com.up.fintech.armagedon.tp4.entity.credit.InternalIn;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

public class BlockedDepositState extends AbstractWalletState {

	public BlockedDepositState(Wallet wallet) {
		super(wallet);
		// TODO Auto-generated constructor stub
	}

	@Override
	public WalletStatusEnum getState() {
		return WalletStatusEnum.BLOCKED_DEPOSIT;
	}

	@Override
	public void closed() {
		var state = new ClosedState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void blocked() {
		var state = new BlockedState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void blockWithdraw() {
		var state = new BlockedWithdrawState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public void blockDeposit() {
	}

	@Override
	public void enabled() {
		var state = new EnabledState(this.wallet);
		wallet.setState(state);
	}

	@Override
	public Transaction executeTransaction(Transaction transaction) {
		if (transaction instanceof InternalIn || transaction instanceof ExternalIn || transaction instanceof Deposit)
			throw new TransactionException("No se puede Despositar/Recibir dinero sobre una cta bloqueada");
		else
			return transaction.execute(this.wallet);
	}

}
