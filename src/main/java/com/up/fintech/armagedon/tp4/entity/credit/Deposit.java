package com.up.fintech.armagedon.tp4.entity.credit;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	
	@Transient @JsonIgnore
	private BufferedImage qr;
	
	public Deposit() {
		super();
		super.setType(TransactionType.DEPOSIT);
		setStrategy(SpringContext.getBean(DepositServiceStrategy.class));
		setNote("Deposito por Ventanilla Completedo Correctamente");
	}
	
	@Override
	public void setAmount(BigDecimal amount) {
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
