package com.up.fintech.armagedon.tp4.entity.state.event;

import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.bet.Event;

public class ClosedState extends AbstractEventState {

	public ClosedState(Event event) {
		super(event);
	}

	@Override
	public EventStatusEnum getState() {
		return EventStatusEnum.CLOSED;
	}

	@Override
	public Event execute(Wallet wallet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeState() {
		// TODO Auto-generated method stub
		
	}
	
	
}
