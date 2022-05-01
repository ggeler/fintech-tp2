package com.up.fintech.armagedon.tp4.entity;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
//@Table(name = "deposits")
public class Deposit extends Transaction {
	
	public Deposit() {
		super();
		super.setType(TransactionType.DEPOSIT);
	}
	
	@Override
	public void setAmount(double amount) {
//		Assert.isTrue(amount > 0, "Amount cant be zero or less");
		super.setAmount(amount);
	}

}
