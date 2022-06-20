package com.up.fintech.armagedon.tp4.entity.credit;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.Debit;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class FeePayment extends Credit {

	@OneToOne @JsonIgnore
	private Debit origin;
//	@JsonProperty(access = Access.READ_ONLY) 
//	private BigDecimal total;
	
	private FeePayment() {
		super.setType(TransactionType.FEEPAY);
//		setStrategy(SpringContext.getBean(FeePaymentServiceStrategy.class));
//		setNote("Cobro Interno y automático por retiro o pago");
	}
	
	public FeePayment(Debit transaction, Wallet wallet) {
		this();
		this.origin = transaction; 
		
		setAmount(transaction.getAmount());
//		setTotal(transaction.getAmount());
		setWallet(wallet);
	}
	
}
