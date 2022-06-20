package com.up.fintech.armagedon.tp4.entity.debit;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.credit.Credit;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.strategy.FeeChargeServiceStrategy;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class FeeCharge extends Debit {

//	@Transient @JsonProperty(access = Access.READ_ONLY) 
//	private BigDecimal percentage = BigDecimal.valueOf(0.02);
//	
	@OneToOne @JsonIgnore
	private Transaction origin;
	
	private FeeCharge() {
		super.setType(TransactionType.FEECHARGE);
		setStrategy(SpringContext.getBean(FeeChargeServiceStrategy.class));
//		setNote("Cargo por retiro o pago");
	}
	
	public FeeCharge(Transaction transaction, Wallet wallet) {
		this();
		this.origin = transaction; 
		this.setAmount(transaction.getAmount().multiply(transaction.getFeeCharge()));
		this.setFee(BigDecimal.ZERO);
		this.setWallet(wallet);
		
		if (wallet.getBalance().compareTo(transaction.getAmount()) == 0) 
			transaction.setAmount(transaction.getAmount().subtract(this.getTotal()));
		
		if (transaction instanceof Debit)
			transaction.setFee(this.getAmount());
		else if (transaction instanceof Credit)
			transaction.setFee(this.getAmount().negate());
		
		if (wallet.getBalance().compareTo(transaction.getTotal())<0)
			throw new TransactionException(String.format("Error: El total de la transacción no puede dejar el balance en negativo - Balance actual: %f - Total= %f + %f",wallet.getBalance(),transaction.getAmount(),transaction.getFee()));
		
	}
	
}
