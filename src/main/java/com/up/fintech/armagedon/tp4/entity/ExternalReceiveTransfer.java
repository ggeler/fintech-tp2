package com.up.fintech.armagedon.tp4.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonView;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.misc.component.Views;
import com.up.fintech.armagedon.tp4.strategy.ExternalReceiveTransferServiceStrategy;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class ExternalReceiveTransfer extends Transaction {
	
	@JsonView(Views.Public.class)
	private String fromCvu;
	
	@JsonProperty(access = Access.READ_ONLY) @JsonView(Views.Internal.class) @JsonInclude(Include.NON_NULL) @OneToOne 
	private ExternalBank externalBank;
	
	@JsonView(Views.Public.class)
	private String toCvu;
	
	@JsonProperty(value = "comment", required = true) @JsonView(Views.Public.class)
	private String externalNote;

	@Transient @JsonAlias("wallet") @JsonView(Views.Internal.class) @JsonInclude(Include.NON_NULL)
	private Wallet walletInformation;

	@Data 
	private class Wallet {
		
		@JsonInclude(Include.NON_NULL)
		private String email;
		
		public Wallet(String email) {
			this.email = email;
		}
	}
	
	public ExternalReceiveTransfer() {
		super();
		super.setType(TransactionType.EXTERNAL_RECEIVE);
		setStrategy(SpringContext.getBean(ExternalReceiveTransferServiceStrategy.class));
		setNote("Transferencia desde Billetera Externa Recibida");
	}
	
	public void setWalletInformation(String email) {
		this.walletInformation = new Wallet(email);
	}
	
}
