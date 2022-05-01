package com.up.fintech.armagedon.tp4.entity;

import java.util.UUID;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class InternalSendTransfer extends Transaction {
	
	@Type(type = "org.hibernate.type.UUIDCharType") private UUID toWallet;
	private String toCvu;
	
	public InternalSendTransfer() {
		super();
		super.setType(TransactionType.INTERNAL_SEND);
	}
}
