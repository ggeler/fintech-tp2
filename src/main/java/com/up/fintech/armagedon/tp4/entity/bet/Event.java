package com.up.fintech.armagedon.tp4.entity.bet;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.entity.state.event.ClosedState;
import com.up.fintech.armagedon.tp4.entity.state.event.EventStatusEnum;
import com.up.fintech.armagedon.tp4.entity.state.event.IEventState;
import com.up.fintech.armagedon.tp4.entity.state.event.OpenState;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
public class Event {
	
	@Setter(value = AccessLevel.NONE) @JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull @Column(unique = true) private long eventKey;
	@NotNull private long awayTeamKey;
	@NotNull private String awayTeam;
	@NotNull private long homeTeamKey;
	@NotNull private String homeTeam;
	private int homeTeamScore;
	private int awayTeamScore;
	@NotNull private LocalDate date;
	@NotNull private LocalTime time;
	@NotNull private String awayTeamLogo;
	@NotNull private String homeTeamLogo;
	
	@JsonProperty(access = Access.READ_ONLY) @NotNull @Enumerated(EnumType.STRING)
	private EventStatusEnum status;
	
	@JsonIgnore @Transient 
	private IEventState state = new OpenState(this);
	
	@OneToMany(mappedBy = "event") @JsonIgnore 
	private List<Bet> bets = new ArrayList<>();

	public void setEventState() {
		switch(status) {
		case CLOSED: 
			state = new ClosedState(this);
			break;
		case OPEN:
			state = new OpenState(this);
			break;
		}
		
	}

}
