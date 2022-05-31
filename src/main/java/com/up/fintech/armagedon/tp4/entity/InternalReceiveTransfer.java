package com.up.fintech.armagedon.tp4.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.CashServiceStrategy;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class InternalReceiveTransfer extends Transaction {
	
	@Type(type = "org.hibernate.type.UUIDCharType") @NotNull 
	private UUID fromWallet;
	
	private InternalReceiveTransfer() {
		super();
		super.setType(TransactionType.INTERNAL_RECEIVE);
		super.setStrategy(SpringContext.getBean(CashServiceStrategy.class));
		setNote("Transferencia desde Billetera misma Compañia Recibida Correctamente");
	}

	public InternalReceiveTransfer(InternalSendTransfer transfer, Wallet wallet) {
//		InternalReceiveTransfer();
		this();
		this.setAmount(transfer.getAmount());
		this.setFromWallet(transfer.getWallet().getWalletId());
		this.setTransactionId(transfer.getTransactionId());
		this.setWallet(wallet);
	}
	
	
}
