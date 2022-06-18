package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.credit.FeePayment;
import com.up.fintech.armagedon.tp4.entity.debit.FeeCharge;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;
import com.up.fintech.armagedon.tp4.service.WalletService;

@Service
public final class FeeChargeServiceStrategy implements ITransactionStrategy {

	private final WalletService service;
	private final ITransactionRepository repository;
	
	@Autowired
	public FeeChargeServiceStrategy(WalletService service, ITransactionRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	
	private Transaction fee(Wallet wallet, FeeCharge fee) throws TransactionException {
		wallet.confirmWithdrawRequest(fee);
		var savedFee = repository.save(fee);
		var feeWallet =  service.getFeeWallet();
		var feePay = new FeePayment(fee,feeWallet);
		feeWallet.directDeposit(feePay);
		repository.save(feePay);
		return savedFee;
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		
		return fee(wallet, (FeeCharge) transaction);
	}

}
