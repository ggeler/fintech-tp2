package com.up.fintech.armagedon.tp4.misc.strategy;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

public interface ITransactionStrategy {

	Transaction execute(Wallet wallet, Transaction transaction);
}
