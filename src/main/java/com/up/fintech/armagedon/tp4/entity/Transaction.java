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
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.up.fintech.armagedon.tp4.misc.component.Views;
import com.up.fintech.armagedon.tp4.misc.state.ITransactionState;
import com.up.fintech.armagedon.tp4.misc.state.NewState;
import com.up.fintech.armagedon.tp4.misc.state.TransactionStatus;
import com.up.fintech.armagedon.tp4.misc.strategy.ITransactionStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name = "transactions")
public abstract class Transaction {

	@Setter(value = AccessLevel.NONE) 
	@JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
//	@Setter(value = AccessLevel.NONE) 
	@JsonProperty(access = Access.READ_ONLY) private Instant timeStampt = Instant.now();
	@Setter(value = AccessLevel.PROTECTED) 
	@JsonProperty(access = Access.READ_ONLY) private TransactionType type;
	@JsonProperty(access = Access.READ_ONLY) private TransactionStatus status;
	@JsonIgnore @Transient private ITransactionState state = new NewState(this);
	@JsonView(Views.Public.class) private double amount;
	@JsonProperty(access = Access.READ_ONLY) @Type(type = "org.hibernate.type.UUIDCharType") private UUID transactionId = UUID.randomUUID();
	@JsonProperty(access = Access.READ_ONLY) private String note;
	@JsonIgnore @OneToOne private Wallet wallet;

	@Transient @JsonIgnore private ITransactionStrategy strategy;
	
	public Transaction execute(Wallet wallet) {
		return strategy.execute(wallet, this);
	}
}

