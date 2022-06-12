package com.up.fintech.armagedon.tp4.entity.credit;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.misc.component.RandomConfirmationCode;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.DepositServiceStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
//@Table(name = "deposits")
public class Deposit extends Credit {
	
	@Setter(value = AccessLevel.NONE) @JsonInclude(Include.NON_NULL)
	private String confirmationCode;
	
	public Deposit() {
		super();
		super.setType(TransactionType.DEPOSIT);
		setStrategy(SpringContext.getBean(DepositServiceStrategy.class));
		setNote("Deposito por Ventanilla Completedo Correctamente");
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
	public void depositRequest() {
		this.setConfirmationCode();
		super.depositRequest();
	}
}
