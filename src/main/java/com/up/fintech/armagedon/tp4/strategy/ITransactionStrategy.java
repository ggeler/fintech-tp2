package com.up.fintech.armagedon.tp4.strategy;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

public sealed interface ITransactionStrategy permits ExternalReceiveTransferServiceStrategy, CashServiceStrategy, InternalSendTransferServiceStrategy, ExternalSendTransferServiceStrategy, ExternalReceiveTransferWithConfirmationServiceStrategy, ExternalReceiveTransferConfirmationServiceStrategy, ExternalReceiveTransferCancelServiceStrategy {

	Transaction execute(Wallet wallet, Transaction transaction);
}
