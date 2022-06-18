package com.up.fintech.armagedon.tp4.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonView;
import com.up.fintech.armagedon.tp4.entity.state.transaction.CancelState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.CompleteState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.DepositState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.ITransactionState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.NewState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.FeeChargeState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.FeePayState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.PendingConfirmationState;
import com.up.fintech.armagedon.tp4.entity.state.transaction.PreviewState;
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
import lombok.Setter;

@Data
@Entity
@Table(name = "transactions", indexes = {
		@Index(columnList = "transactionId, createdTime")		
})
//@JsonView(Views.Public.class)  TEST
public abstract class Transaction {

	@Setter(value = AccessLevel.NONE) 
	@JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) @NotNull 
	private Instant createdTime = Instant.now();
	
	@JsonProperty(access = Access.READ_ONLY) @JsonInclude(Include.NON_NULL)
	private Instant confirmedTime;
	
	@JsonProperty(access = Access.READ_ONLY) @JsonInclude(Include.NON_NULL)
	private Instant canceledTime;
	
//	@Setter(value = AccessLevel.PROTECTED) 
	@JsonProperty(access = Access.READ_ONLY) @NotNull @Enumerated(EnumType.STRING)
	private TransactionType type;

	@JsonProperty(access = Access.READ_ONLY) @NotNull @Enumerated(EnumType.STRING)
	private TransactionStatusEnum status;
	
	@JsonIgnore @Transient 
	private ITransactionState state = new NewState(this);
	
	@JsonView(Views.Public.class) 
	private BigDecimal amount = new BigDecimal(0.0);
	
	@JsonProperty(access = Access.READ_ONLY) @Type(type = "org.hibernate.type.UUIDCharType") @NotNull 
	private UUID transactionId = UUID.randomUUID();
	
	@JsonProperty(access = Access.READ_ONLY) @JsonInclude(Include.NON_NULL)
	private String note;
	
	@JsonIgnore @ManyToOne(optional = false) //@JoinColumn(name = "fk_wallet_id", insertable = false, updatable = false, unique = false )
	protected Wallet wallet;

//	@Getter(value = AccessLevel.NONE) 
	@Transient @JsonIgnore 
	private ITransactionStrategy strategy;
	
	public Transaction execute(Wallet wallet) {
		return state.execute(wallet);
	}

	public void setTransactionState() {
		switch (status) {
		case CANCELED:
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
		case PREVIEW:
			state = new PreviewState(this);
			break;
		case CHARGINGFEE:
			state = new FeeChargeState(this);
			break;
		case PAYINGFEE:
			state = new FeePayState(this);
			break;
		
		}
	}
}

