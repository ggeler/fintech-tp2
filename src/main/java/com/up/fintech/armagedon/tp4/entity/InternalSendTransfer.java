package com.up.fintech.armagedon.tp4.entity;

import java.util.UUID;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.InternalSendTransferServiceStrategy;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class InternalSendTransfer extends Transaction {
	
	@Type(type = "org.hibernate.type.UUIDCharType") @JsonInclude(Include.NON_NULL) 
	private UUID toWallet;
	
	@JsonInclude(Include.NON_NULL)
	private String toCvu;
	
	public InternalSendTransfer() {
		super();
		super.setType(TransactionType.INTERNAL_SEND);
		setStrategy(SpringContext.getBean(InternalSendTransferServiceStrategy.class));
		setNote("Transferencia a Billetera Misma Compañía Completeda");
	}
}
