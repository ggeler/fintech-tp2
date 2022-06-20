package com.up.fintech.armagedon.tp4.entity.state.event;

import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.bet.Event;

public interface IEventState {
	public EventStatusEnum getState();
	public Event execute(Wallet wallet); 
	public void changeState();
}
