package com.up.fintech.armagedon.tp4.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.repository.ITransactionRepository;
import com.up.fintech.armagedon.tp4.service.WalletService;

@Service
public final class WithdrawConfirmationServiceStrategy implements ITransactionStrategy {

	private final WalletService service;
	private final ITransactionRepository repository;
	
	@Autowired
	public WithdrawConfirmationServiceStrategy(WalletService service, ITransactionRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	
	private Transaction confirmWithdraw(Wallet wallet, Withdraw withdraw) throws TransactionException {
		withdraw.setWallet(wallet);
		try {
			var fee = withdraw.getFeeTransaction();
			fee.setTransactionState();
//			var fee = feeRepository.findByOrigin(withdraw).orElseThrow(() -> new TransactionException("No se encontró el fee correspondiente"));
			wallet.confirmWithdrawRequest(withdraw);
			var savedDeposit = repository.save(withdraw);
			service.save(wallet);
			fee.execute(wallet);
			return savedDeposit;
		} catch (TransactionException e) {
			throw e;
		} 
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return confirmWithdraw(wallet, (Withdraw) transaction);
	}

}
