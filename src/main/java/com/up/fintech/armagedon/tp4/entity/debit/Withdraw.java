package com.up.fintech.armagedon.tp4.entity.debit;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.misc.component.RandomConfirmationCode;
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
	
	@Setter(value = AccessLevel.NONE) @JsonInclude(Include.NON_NULL)
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
		confirmationCode = RandomConfirmationCode.generateRandomCode();
	}

	@Override
	public void withdrawRequest() {
		this.setConfirmationCode();
		super.withdrawRequest();
	}
}
