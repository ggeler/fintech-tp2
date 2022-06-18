package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.credit.FeePayment;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;
import com.up.fintech.armagedon.tp4.service.WalletService;

@Service
public final class BORRARFeePaymentServiceStrategy {//implements ITransactionStrategy {

	private final WalletService service;
	private final ITransactionRepository repository;
	
	@Autowired
	public BORRARFeePaymentServiceStrategy(WalletService service, ITransactionRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	
	private Transaction fee(Wallet wallet, FeePayment fee) throws TransactionException {
		fee.getState().changeState();
//		feeWallet.confirmDepositRequest(fee);
//		feeWallet.execute(fee);
		repository.save(fee);
		service.save(wallet);
		return fee;
	}
	
//	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		System.out.println("Nunca pasé?");
		return fee(wallet, (FeePayment) transaction);
	}

}
