package com.up.fintech.armagedon.tp4.entity.debit;

import javax.persistence.Entity;

import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.WithdrawServiceStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
//@Table(name = "deposits")
public class Withdraw extends Debit {
	
	@Setter(value = AccessLevel.NONE) 
	private String confirmationCode;
	
	public Withdraw() {
		super();
		super.setType(TransactionType.WITHDRAW);
		setStrategy(SpringContext.getBean(WithdrawServiceStrategy.class));
		setNote("Retiro por Ventanilla Completedo Correctamente");
	}
	
	@Override
	public void setAmount(double amount) {
//		Assert.isTrue(amount > 0, "Amount cant be zero or less");
		super.setAmount(amount);
	}
	
	public void setConfirmationCode() {
		confirmationCode = "ABCDEF";
	}

	@Override
	public void withdrawRequest() {
		this.setConfirmationCode();
		super.withdrawRequest();
	}
}
