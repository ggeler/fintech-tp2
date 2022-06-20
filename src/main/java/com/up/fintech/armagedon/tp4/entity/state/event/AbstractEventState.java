package com.up.fintech.armagedon.tp4.entity.state.event;

import com.up.fintech.armagedon.tp4.entity.bet.Event;

public abstract class AbstractEventState implements IEventState {

	protected Event event;
	
	public AbstractEventState(Event event) {
		this.event = event;
		event.setStatus(this.getState());
	}
	
	
}
