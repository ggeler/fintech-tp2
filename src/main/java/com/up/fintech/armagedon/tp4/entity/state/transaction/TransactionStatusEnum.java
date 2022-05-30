package com.up.fintech.armagedon.tp4.entity.state.transaction;

public enum TransactionStatusEnum {

	SENDING("enviando"),
	RECEIVING("recibiendo"),
	DEPOSITING("Depositando"),
	WITHDRAWING("Extrayendo"),
	CANCEL("cancelada"),
	REJECTED("rechazada"),
	COMPLETED("completada"),
	NEW("nueva");
	
	public String status;
	
	private TransactionStatusEnum(String status) {
		
		this.status = status;
	}
}
