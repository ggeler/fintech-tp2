package com.up.fintech.armagedon.tp4.entity.debit;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.DebitBetServiceStrategy;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class DebitBet extends Debit {

	@OneToOne @JsonIgnore
	private Transaction origin;
	
	private DebitBet() {
		super();
		super.setType(TransactionType.DEBITBET);
		setStrategy(SpringContext.getBean(DebitBetServiceStrategy.class));
	}
	
	public DebitBet(Wallet wallet, BigDecimal amount, Transaction transaction) {
		this();
		this.wallet=wallet;
		setAmount(amount);
		origin = transaction;
	}
}
