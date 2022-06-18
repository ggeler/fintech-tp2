package com.up.fintech.armagedon.tp4.entity.debit;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.FeeChargeServiceStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class FeeCharge extends Debit {

	@Transient
	private BigDecimal percentage = BigDecimal.valueOf(0.02);
	
	@OneToOne @JsonIgnore
	private Debit origin;
	
	private FeeCharge() {
		super.setType(TransactionType.FEECHARGE);
		setStrategy(SpringContext.getBean(FeeChargeServiceStrategy.class));
		setNote("Cargo por retiro o pago");
	}
	
	public FeeCharge(Debit transaction, Wallet wallet) {
		this();
		this.origin = transaction; 
//		setFee(transaction.getAmount()*percentage);
		setTotal(transaction.getAmount().multiply(percentage));
		setAmount(getTotal());
		if (wallet.getBalance().compareTo(transaction.getAmount()) == 0) 
			transaction.setAmount(transaction.getAmount().subtract(this.getTotal()));
		transaction.setTotal(transaction.getAmount().add(this.getTotal()));
		transaction.setFee(getTotal());
		
		setWallet(wallet);
	}
	
}
