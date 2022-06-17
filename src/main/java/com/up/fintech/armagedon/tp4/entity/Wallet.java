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
import com.up.fintech.armagedon.tp4.entity.credit.Credit;
import com.up.fintech.armagedon.tp4.entity.debit.Debit;
import com.up.fintech.armagedon.tp4.entity.debit.ExternalOut;
import com.up.fintech.armagedon.tp4.entity.debit.InternalOut;
import com.up.fintech.armagedon.tp4.entity.state.wallet.BlockedDepositState;
import com.up.fintech.armagedon.tp4.entity.state.wallet.BlockedWithdrawState;
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
@Table(name = "wallets") //Test
public class Wallet {

	@Setter(value = AccessLevel.NONE) @JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) @Column(unique = true) @Type(type = "org.hibernate.type.UUIDCharType") @NotNull 
	private UUID walletId = UUID.randomUUID();
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) @OneToOne(cascade = CascadeType.ALL) @JoinColumn(unique = true) @NotNull 
	private User user = new User();
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) @OneToOne(cascade = CascadeType.ALL) @JoinColumn(unique = true) @NotNull 
	private Cvu cvu = new Cvu(this);
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) 
	private double balance = 0.0;
	
	@Setter(value = AccessLevel.NONE) @JsonIgnore @OneToMany(cascade = CascadeType.ALL, mappedBy = "wallet" ) //@JoinColumn(name = "fk_wallet_id", nullable = false) 
	private List<Transaction> transactions = new ArrayList<>();
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) 
	private Instant creationTime = Instant.now();
	
	@Setter(value = AccessLevel.NONE) @JsonProperty(access = Access.READ_ONLY) 
	private Instant lastTransactionTime = transactions.stream().map(Transaction::getCreatedTime).max(Instant::compareTo).orElse(Instant.EPOCH); 
	
	@JsonProperty(access = Access.READ_ONLY) 
	private WalletStatusEnum status;
	
	@JsonIgnore @Transient 
	private IWalletState state = new EnabledState(this);
	
//	public void directDeposit(Credit transaction) throws TransactionException {
//		balance = transaction.directDeposit();
//	}

	public void confirmDepositRequest(Credit transaction) throws TransactionException {
		balance = transaction.confirmDepositRequest();
	}
	
	public void directWithdraw(Debit transaction) throws TransactionException {
		balance = transaction.directWithdraw();
	}
	
	public void directDeposit(Credit transaction) throws TransactionException {
		balance = transaction.directDeposit();
	}

	public void confirmWithdrawRequest(Debit transaction) throws TransactionException {
		balance = transaction.confirmWithdrawRequest();
	}
	
	public Transaction execute(Transaction transaction) throws TransactionException {
		if (transaction instanceof InternalOut tmp) {
			if (tmp.getToCvu()!=null && !tmp.getToCvu().isEmpty() && !Cvu.isInternal(tmp.getToCvu())) {
				transaction = new ExternalOut(tmp);
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
			case BLOCKED_DEPOSIT:
				this.setState(new BlockedDepositState(this));
				break;
			case BLOCKED_WITHDRAW:
				this.setState(new BlockedWithdrawState(this));
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
