package com.up.fintech.armagedon.tp4.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class InternalReceiveTransfer extends Transaction {
	
	@Type(type = "org.hibernate.type.UUIDCharType") @NotNull private UUID fromWallet;
	
	public InternalReceiveTransfer() {
		super();
		super.setType(TransactionType.INTERNAL_RECEIVE);
	}
	
	
}
