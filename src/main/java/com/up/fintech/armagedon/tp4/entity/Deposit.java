package com.up.fintech.armagedon.tp4.entity;

import javax.persistence.Entity;

import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.misc.strategy.CashServiceStrategy;

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
		setStrategy(SpringContext.getBean(CashServiceStrategy.class));
		setNote("Deposito por Ventanilla Completedo Correctamente");
	}
	
	@Override
	public void setAmount(double amount) {
//		Assert.isTrue(amount > 0, "Amount cant be zero or less");
		super.setAmount(amount);
	}

}
