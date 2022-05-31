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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.up.fintech.armagedon.tp4.entity.state.transaction.CancelState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.CompleteState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.DepositState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.ITransactionState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.NewState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.PendingConfirmationState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.ReceivingState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.ReceivingWithConfirmationState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.RejectedState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.SendingState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.TransactionStatusEnum;
import com.up.fintech.armagedon.tp4.entity.state.transaction.WithDrawingState;
import com.up.fintech.armagedon.tp4.misc.component.Views;
import com.up.fintech.armagedon.tp4.strategy.ITransactionStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "transactions")
//@JsonView(Views.Public.class) 
public abstract class Transaction {

	@Setter(value = AccessLevel.NONE) 
	@JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Setter(value = AccessLevel.NONE) 
	@JsonProperty(access = Access.READ_ONLY) 
	private Instant timeStampt = Instant.now();
	
//	@Setter(value = AccessLevel.PROTECTED) 
	@JsonProperty(access = Access.READ_ONLY) 
	private TransactionType type;

	@JsonProperty(access = Access.READ_ONLY) 
	private TransactionStatusEnum status;
	
	@JsonIgnore @Transient 
	private ITransactionState state = new NewState(this);
	
	@JsonView(Views.Public.class) 
	private double amount;
	
	@JsonProperty(access = Access.READ_ONLY) @Type(type = "org.hibernate.type.UUIDCharType") 
	private UUID transactionId = UUID.randomUUID();
	
	@JsonProperty(access = Access.READ_ONLY) @JsonInclude(Include.NON_NULL)
	private String note;
	
	@JsonIgnore @OneToOne 
	private Wallet wallet;

	@Getter(value = AccessLevel.NONE) @Transient @JsonIgnore 
	private ITransactionStrategy strategy;
	
	public Transaction execute(Wallet wallet) {
		return strategy.execute(wallet, this);
	}

	public void setTransactionState() {
		switch (status) {
		case CANCEL:
			state = new CancelState(this);
			break;
		case COMPLETED:
			state = new CompleteState(this);
			break;
		case DEPOSITING:
			state = new DepositState(this);
			break;
		case NEW:
			state = new NewState(this);
			break;
		case PENDING_CONFIRMATION:
			state = new PendingConfirmationState(this);
			break;
		case RECEIVING:
			state = new ReceivingState(this);
			break;
		case RECEIVING_WITH_CONFIRMATION:
			state = new ReceivingWithConfirmationState(this);
			break;
		case REJECTED:
			state = new RejectedState(this);
			break;
		case SENDING:
			state = new SendingState(this);
			break;
		case WITHDRAWING:
			state = new WithDrawingState(this);
			break;
		}
	}
}

