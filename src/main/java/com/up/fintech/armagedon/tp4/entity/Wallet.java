package com.up.fintech.armagedon.tp4.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.up.fintech.armagedon.tp4.entity.state.wallet.BlockedForReceiveState;
import com.up.fintech.armagedon.tp4.entity.state.wallet.BlockedForSendState;
import com.up.fintech.armagedon.tp4.entity.state.wallet.BlockedState;
import com.up.fintech.armagedon.tp4.entity.state.wallet.ClosedState;
import com.up.fintech.armagedon.tp4.entity.state.wallet.EnabledState;
import com.up.fintech.armagedon.tp4.entity.state.wallet.IWalletState;
import com.up.fintech.armagedon.tp4.entity.state.wallet.WalletStatusEnum;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallets")
public class Wallet {

	@Setter(value = AccessLevel.NONE) @JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) @Column(unique = true) @Type(type = "org.hibernate.type.UUIDCharType") @NotNull 
	private UUID walletId = UUID.randomUUID();
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) @OneToOne(cascade = CascadeType.ALL) @NotNull 
	private User user = new User();
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) @OneToOne(cascade = CascadeType.ALL) @JoinColumn(unique = true) @NotNull 
	private Cvu cvu = new Cvu(this);
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) 
	private double balance = 0.0;
	
	@Setter(value = AccessLevel.NONE) @JsonIgnore @OneToMany(cascade = CascadeType.ALL) @JoinColumn(name = "fk_wallet_id", nullable = false) 
	private List<Transaction> transactions = new ArrayList<>();
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) 
	private Instant creationTime = Instant.now();
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) 
	private Instant lastTransactionTime = transactions.stream().map(Transaction::getTimeStampt).max(Instant::compareTo).orElse(Instant.EPOCH); 
	
	@JsonProperty(access = Access.READ_ONLY) 
	private WalletStatusEnum status;
	
	@JsonIgnore @Transient 
	private IWalletState state = new EnabledState(this);
	
	public void deposit(Transaction transaction) throws TransactionException {
		if (this.getStatus()==WalletStatusEnum.BLOCKED || this.getStatus()==WalletStatusEnum.BLOCKED_RECEIVE)
			throw new TransactionException("Cuenta bloqueada para depósito");
		if (transaction.getAmount()>0) {
			transaction.getState().changeState();
			transactions.add(transaction);
			var newAmount = this.balance+transaction.getAmount();
			balance = newAmount;
			transaction.getState().changeState();
//			transaction.setNote("Deposit transaction completed");
		} else {
			transaction.getState().reject();
			transactions.add(transaction);
			var note = "Rejected: amount to deposit cant be negative or zero "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
	}

	public void receiveDeposit(Transaction transaction) throws TransactionException {
		if (this.getStatus()==WalletStatusEnum.BLOCKED || this.getStatus()==WalletStatusEnum.BLOCKED_RECEIVE)
			throw new TransactionException("Cuenta bloqueada para depósito");
		if (transaction.getAmount()>0) {
			transaction.getState().changeState();
			transactions.add(transaction);
			transaction.getState().changeState();
			transaction.setNote("Receive transaction pending confirmation");
		} else {
			transaction.getState().reject();
			transactions.add(transaction);
			var note = "Rejected: amount to deposit cant be negative or zero "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
	}
	public void cancelDeposit(Transaction transaction) {
		transaction.getState().cancel();
		transaction.setNote("Transacción Externa Cancelada");
	}
	
	public void confirmDeposit(Transaction transaction) throws TransactionException {
		if (this.getStatus()==WalletStatusEnum.BLOCKED || this.getStatus()==WalletStatusEnum.BLOCKED_RECEIVE)
			throw new TransactionException("Cuenta bloqueada para depósito");
		if (transaction.getAmount()>0) {
			transaction.getState().changeState();
			var newAmount = this.balance+transaction.getAmount();
			balance = newAmount;
			transaction.setNote("Transacción Externa Confirmada");
		} else {
			transaction.getState().reject();
			transactions.add(transaction);
			var note = "Rejected: amount to deposit cant be negative or zero "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
	}
	
	public void debit(Transaction transaction) throws TransactionException {
		if (this.getStatus()==WalletStatusEnum.BLOCKED || this.getStatus()==WalletStatusEnum.BLOCKED_SEND)
			throw new TransactionException("Cuenta bloqueada para débito");
		if (transaction.getAmount()>0 && balance >= transaction.getAmount()) {
			transaction.getState().changeState();
			transactions.add(transaction);
			var newAmount = this.balance-transaction.getAmount();
			balance = newAmount;
			transaction.getState().changeState();
		} else {
			transaction.getState().reject();
			transactions.add(transaction);
			var note = "Rejected: balance less than requested transaction or zero/negative amount requested "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
	}
	
	public Transaction execute(Transaction transaction) {
		if (transaction instanceof InternalSendTransfer tmp) {
			if (tmp.getToCvu()!=null && !tmp.getToCvu().isEmpty() && !Cvu.isInternal(tmp.getToCvu())) {
				transaction = new ExternalSendTransfer(tmp);
			}
		}
		return state.executeTransaction(transaction);
	}
	
	public void setWalletState() {
		if (this.getStatus()!=null) {
			switch (this.getStatus()) {
			case BLOCKED: 
				this.setState(new BlockedState(this));
				break;
			case BLOCKED_RECEIVE:
				this.setState(new BlockedForReceiveState(this));
				break;
			case BLOCKED_SEND:
				this.setState(new BlockedForSendState(this));
				break;
			case CLOSED:
				this.setState(new ClosedState(this));
				break;
			case ENABLED:
				this.setState(new EnabledState(this));
				break;
			}
		}
	}

}
