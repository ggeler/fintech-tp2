package com.up.fintech.armagedon.tp4.entity.debit;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.up.fintech.armagedon.tp4.entity.ExternalBank;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.misc.component.RandomConfirmationCode;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.ExternalSendTransferServiceStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class ExternalOut extends Debit {
	private static final BigDecimal comissionFee = BigDecimal.valueOf(0.07);  
//	@JsonProperty(access = Access.READ_ONLY) private String fromCvu;
	private String toCvu;
	
	@JsonProperty(access = Access.READ_ONLY) @OneToOne 
	private ExternalBank externalBank;
	
	@Setter(value = AccessLevel.NONE) @JsonInclude(Include.NON_NULL)
	private String confirmationCode;
	
	public ExternalOut() {
		super();
		super.setType(TransactionType.EXTERNAL_SEND);
		setStrategy(SpringContext.getBean(ExternalSendTransferServiceStrategy.class));
//		setNote("Enviando dinero fuera de la PSP");
		setFeeCharge(comissionFee);
	}

	public ExternalOut(InternalOut tmp) {
		this();
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
