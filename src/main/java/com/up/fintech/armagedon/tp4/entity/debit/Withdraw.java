package com.up.fintech.armagedon.tp4.entity.debit;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.entity.state.transaction.PreviewState;
import com.up.fintech.armagedon.tp4.misc.component.RandomConfirmationCode;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.WithdrawRequestServiceStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
//@Table(name = "deposits")
public class Withdraw extends Debit {
	
	private static final BigDecimal comissionFee = BigDecimal.valueOf(0.055);   
	
	@Setter(value = AccessLevel.NONE) @JsonInclude(Include.NON_NULL)
	private String confirmationCode;
	
	@Transient @JsonIgnore
	private BufferedImage qr;
	
	@OneToOne @JsonIgnore
	private FeeCharge feeTransaction;
	
	public Withdraw() {
		super();
		super.setType(TransactionType.WITHDRAW);
		setStrategy(SpringContext.getBean(WithdrawRequestServiceStrategy.class));
//		setNote("Retiro por Ventanilla Completedo Correctamente");
		super.setFeeCharge(comissionFee);
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
	public void withdrawRequest() {
		if (!(getState() instanceof PreviewState))
			this.setConfirmationCode();
		super.withdrawRequest();
	}
}
