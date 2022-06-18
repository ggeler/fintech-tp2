package com.up.fintech.armagedon.tp4.strategy;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

public sealed interface ITransactionStrategy permits DepositServiceStrategy, 
	InternalSendTransferServiceStrategy, ExternalSendTransferServiceStrategy, ExternalReceiveTransferWithConfirmationServiceStrategy, 
	ExternalReceiveTransferConfirmationServiceStrategy, ExternalReceiveTransferCancelServiceStrategy, WithdrawRequestServiceStrategy, 
	WithdrawCancelServiceStrategy, WithdrawConfirmationServiceStrategy, DepositCancelServiceStrategy, DepositConfirmServiceStrategy, 
	QrServiceStrategy, InternalReceiveServiceStrategy, FeeChargeServiceStrategy, WithdrawPreviewServiceStrategy {

	Transaction execute(Wallet wallet, Transaction transaction);
}
