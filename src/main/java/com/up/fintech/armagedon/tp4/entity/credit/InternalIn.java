package com.up.fintech.armagedon.tp4.entity.credit;

import java.util.UUID;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.Debit;
import com.up.fintech.armagedon.tp4.entity.debit.InternalOut;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.DepositServiceStrategy;
import com.up.fintech.armagedon.tp4.strategy.InternalReceiveServiceStrategy;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class InternalIn extends Credit {
	
	@Type(type = "org.hibernate.type.UUIDCharType") @NotNull 
	private UUID fromWallet;
	
	private InternalIn() {
		super();
		super.setType(TransactionType.INTERNAL_RECEIVE);
		super.setStrategy(SpringContext.getBean(InternalReceiveServiceStrategy.class));
		setNote("Transferencia desde Billetera misma Compañia Recibida Correctamente");
	}

	public InternalIn(InternalOut transfer, Wallet wallet) {
//		InternalReceiveTransfer();
		this();
		this.setAmount(transfer.getAmount());
		this.setFromWallet(transfer.getWallet().getWalletId());
		this.setTransactionId(transfer.getTransactionId());
		this.setWallet(wallet);
	}
	
	
}
