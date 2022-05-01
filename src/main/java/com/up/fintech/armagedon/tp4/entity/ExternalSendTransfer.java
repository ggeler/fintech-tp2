package com.up.fintech.armagedon.tp4.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class ExternalSendTransfer extends Transaction {
	
//	@JsonProperty(access = Access.READ_ONLY) private String fromCvu;
	private String toCvu;
	@JsonProperty(access = Access.READ_ONLY) @OneToOne private ExternalBank externalBank;
	
	public ExternalSendTransfer() {
		super();
		super.setType(TransactionType.EXTERNAL_SEND);
	}
}
