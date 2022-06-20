package com.up.fintech.armagedon.tp4.entity.state.event;

public enum EventStatusEnum {
	OPEN("Abierto"),
	CLOSED("Cerrado");

	public String status;
	
	private EventStatusEnum(String status) {
		this.status = status;
	}
}
