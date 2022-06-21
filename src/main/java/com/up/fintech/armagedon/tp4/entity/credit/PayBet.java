package com.up.fintech.armagedon.tp4.entity.credit;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.PayBetServiceStrategy;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
//@Table(name = "deposits")
public class PayBet extends Credit {

	private static final BigDecimal comissionFee = BigDecimal.valueOf(0.04);   
	
	@OneToOne @JsonIgnore
	private Transaction origin;
	
	private PayBet() {
		super();
		super.setType(TransactionType.PAYBET);
		setStrategy(SpringContext.getBean(PayBetServiceStrategy.class));
		super.setFeeCharge(comissionFee);
	}
	
	public PayBet(Wallet wallet, BigDecimal amount, Transaction transaction) {
		this();
		this.wallet=wallet;
		this.setAmount(amount);
		origin = transaction;
	}
	
	
}
