package com.up.fintech.armagedon.tp4.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.credit.Credit;
import com.up.fintech.armagedon.tp4.entity.credit.InternalIn;
import com.up.fintech.armagedon.tp4.entity.debit.InternalOut;
import com.up.fintech.armagedon.tp4.misc.error.CvuException;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.repository.IWalletRepository;
import com.up.fintech.armagedon.tp4.service.WalletService;

@Service
public final class PayBetServiceStrategy implements ITransactionStrategy {

	private final WalletService service;
	
	public PayBetServiceStrategy( WalletService service) {
		this.service = service;
	}
	
	private InternalOut pay(Wallet wallet, Credit deposit) throws UserNotFoundException, WalletNotFoundException, TransactionException, CvuException {

		return null;
	}
	
	@Override
	public Transaction execute(Wallet wallet, Transaction transaction) {
		return pay(wallet, (Credit) transaction);
	}
}
