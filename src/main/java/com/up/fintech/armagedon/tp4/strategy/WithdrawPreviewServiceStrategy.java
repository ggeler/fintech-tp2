package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.FeeCharge;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;

@Service
public final class WithdrawPreviewServiceStrategy implements ITransactionStrategy {
	
	private Transaction preview(Wallet wallet, Withdraw withdraw) throws UserNotFoundException, WalletNotFoundException, TransactionException {
		var fee = new FeeCharge(withdraw, wallet);
		withdraw.setWallet(wallet);
		withdraw.withdrawRequest();
		withdraw.setFeeTransaction(fee);
		return withdraw;
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return preview(wallet, (Withdraw) transaction);
	}

}
