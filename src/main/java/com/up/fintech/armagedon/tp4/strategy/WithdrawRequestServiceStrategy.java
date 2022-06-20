package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.FeeCharge;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;
import com.up.fintech.armagedon.tp4.service.WalletService;

@Service
public final class WithdrawRequestServiceStrategy implements ITransactionStrategy {

	private final WalletService service;
	private final ITransactionRepository repository;
	
	@Autowired
	public WithdrawRequestServiceStrategy(WalletService service, ITransactionRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	
	private Transaction withdraw(Wallet wallet, Withdraw withdraw) throws TransactionException {
		withdraw.setWallet(wallet);
		try {
			withdraw.withdrawRequest();
			var fee = new FeeCharge(withdraw,wallet);
			fee.withdrawRequest();
			withdraw.setFeeTransaction(fee);
//			withdraw.setFee(fee.getTotal());
			repository.save(fee);
			var savedDeposit = repository.save(withdraw);
			service.save(wallet);
			return savedDeposit;
		} catch (TransactionException e) {
			throw e;
		} 
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return withdraw(wallet, (Withdraw) transaction);
	}

}
