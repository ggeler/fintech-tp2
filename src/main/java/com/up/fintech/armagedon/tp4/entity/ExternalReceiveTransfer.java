package com.up.fintech.armagedon.tp4.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonView;
import com.up.fintech.armagedon.tp4.misc.component.Views;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class ExternalReceiveTransfer extends Transaction {
	
//	@JsonProperty(access = Access.READ_ONLY) private String fromCvu;
	@JsonView(Views.Public.class)
	private String fromCvu;
	@JsonProperty(access = Access.READ_ONLY) @JsonView(Views.Internal.class) 
	@OneToOne private ExternalBank externalBank;
	@JsonView(Views.Public.class)
	private String toCvu;
	@JsonProperty(value = "comment", required = true) @JsonView(Views.Public.class)
	private String externalNote;
	
	public ExternalReceiveTransfer() {
		super();
		super.setType(TransactionType.EXTERNAL_RECEIVE);
	}
}
