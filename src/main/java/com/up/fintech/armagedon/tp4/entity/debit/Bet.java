package com.up.fintech.armagedon.tp4.entity.debit;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.entity.bet.Event;
import com.up.fintech.armagedon.tp4.entity.state.transaction.PreviewState;
import com.up.fintech.armagedon.tp4.misc.component.RandomConfirmationCode;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.strategy.BetRequestReceiveServiceStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Bet extends Debit {

	@Setter(value = AccessLevel.NONE) @JsonInclude(Include.NON_NULL)
	private String confirmationCode;
	
	@ManyToOne(cascade = CascadeType.ALL) private Event event;
	private int awayTeamScore;
	private int homeTeamScore;
	
	public Bet() {
		super();
		super.setType(TransactionType.BET);
		setStrategy(SpringContext.getBean(BetRequestReceiveServiceStrategy.class));
	}

	public void setConfirmationCode() {
		confirmationCode = RandomConfirmationCode.generateRandomCode();
	}
	
	@Override
	public void withdrawRequest() {
		if (!(getState() instanceof PreviewState))
			this.setConfirmationCode();
		super.withdrawRequest();
	}
}
