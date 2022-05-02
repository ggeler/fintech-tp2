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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "wallets")
public class Wallet {

	@JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@JsonProperty(access = Access.READ_ONLY) @Column(unique = true) @Type(type = "org.hibernate.type.UUIDCharType") @NotNull 
	private UUID walletId = UUID.randomUUID();
	@JsonProperty(access = Access.READ_ONLY) @OneToOne(cascade = CascadeType.ALL) @NotNull 
	private User user = new User();
	@JsonProperty(access = Access.READ_ONLY) @OneToOne(cascade = CascadeType.ALL) @JoinColumn(unique = true) @NotNull 
	private Cvu cvu = new Cvu(this);
	@JsonProperty(access = Access.READ_ONLY) 
	private double balance = 0.0;
	@JsonIgnore @OneToMany(cascade = CascadeType.ALL) @JoinColumn(name = "fk_wallet_id", nullable = false) 
	private List<Transaction> transactions = new ArrayList<>();
	@JsonProperty(access = Access.READ_ONLY) 
	private Instant creationTime = Instant.now();
	@JsonProperty(access = Access.READ_ONLY) 
	private Instant lastTransactionTime = transactions.stream().map(Transaction::getTimeStampt).max(Instant::compareTo).orElse(Instant.EPOCH); 
	
	public void deposit(Transaction transaction) throws TransactionException {
		if (transaction.getAmount() >0) {
			transaction.getState().changeState();
			transactions.add(transaction);
			var newAmount = this.balance+transaction.getAmount();
			balance = newAmount;
			transaction.getState().changeState();
			transaction.setNote("Deposit transaction completed");
		} else {
			transaction.getState().reject();
			transactions.add(transaction);
			var note = "Rejecte: amount to deposit cant be negative or zero "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
	}

	public void debit(Transaction transaction) throws TransactionException {
		if (transaction.getAmount()>0 && balance >= transaction.getAmount()) {
			transaction.getState().changeState();
			transactions.add(transaction);
			var newAmount = this.balance-transaction.getAmount();
			balance = newAmount;
			transaction.getState().changeState();
			transaction.setNote("Debit transaction completed");
		} else {
			transaction.getState().reject();
			transactions.add(transaction);
			var note = "Rejected: balance less than requested transaction or zero/negative amount requests "+transaction.getAmount();
			transaction.setNote(note);
			throw new TransactionException(note);
		}
	}
	
}
