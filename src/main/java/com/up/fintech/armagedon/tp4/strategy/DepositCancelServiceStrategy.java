package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.credit.Credit;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.service.TransactionService;
import com.up.fintech.armagedon.tp4.service.WalletService;

@Service
public final class DepositCancelServiceStrategy implements ITransactionStrategy {

	private final WalletService walletService;
	private final TransactionService transactionService;
	
	@Autowired
	public DepositCancelServiceStrategy(WalletService walletService, TransactionService transactionService) {
		this.walletService = walletService;
		this.transactionService = transactionService;

	}
	
	private Transaction cancelDeposit(Wallet wallet, Credit deposit) throws UserNotFoundException, WalletNotFoundException, TransactionException {
		deposit.setWallet(wallet);
		try {
			deposit.cancelDepositRequest();
//			wallet.cancelDepositRequest(deposit);
			var savedDeposit = transactionService.save(deposit);
			walletService.save(wallet);
			return savedDeposit;
		} catch (TransactionException e) {
			throw e;
		} 
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return cancelDeposit(wallet, (Credit) transaction);
	}

}
