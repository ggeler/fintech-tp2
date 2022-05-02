package com.up.fintech.armagedon.tp4.entity;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.up.fintech.armagedon.tp4.misc.state.ITransactionState;
import com.up.fintech.armagedon.tp4.misc.state.NewState;
import com.up.fintech.armagedon.tp4.misc.state.TransactionStatus;

import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public abstract class Transaction {

	@JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@JsonProperty(access = Access.READ_ONLY) private Instant timeStampt = Instant.now();
	@JsonProperty(access = Access.READ_ONLY) private TransactionType type;
	@JsonProperty(access = Access.READ_ONLY) private TransactionStatus status;
	@JsonIgnore @Transient private ITransactionState state = new NewState(this);
	private double amount;
	@JsonProperty(access = Access.READ_ONLY) @Type(type = "org.hibernate.type.UUIDCharType") private UUID transactionId = UUID.randomUUID();
	@JsonProperty(access = Access.READ_ONLY) private String note;
	@JsonIgnore @OneToOne private Wallet wallet;
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}

