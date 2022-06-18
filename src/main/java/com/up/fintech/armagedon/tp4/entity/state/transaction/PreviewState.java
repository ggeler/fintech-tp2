package com.up.fintech.armagedon.tp4.entity.state.transaction;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.WithdrawPreviewServiceStrategy;

public class PreviewState extends AbstractTransactionState {

	public PreviewState(Transaction transaction) {
		super(transaction);
	}
	
	@Override
	public void changeState() {
	}

	@Override
	public TransactionStatusEnum getState() {
		return TransactionStatusEnum.PREVIEW;
	}

	@Override
	public Transaction execute(Wallet wallet) {
		if (transaction instanceof Withdraw) 
			transaction.setStrategy(SpringContext.getBean(WithdrawPreviewServiceStrategy.class));
		return transaction.getStrategy().execute(wallet, this.transaction);
	}
}
