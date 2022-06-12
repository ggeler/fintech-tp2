package com.up.fintech.armagedon.tp4.entity.state.transaction;

public enum TransactionStatusEnum {

	SENDING("enviando"),
	RECEIVING("recibiendo"),
	RECEIVING_WITH_CONFIRMATION("Recibiendo con Confirmación"),
	DEPOSITING("Depositando"),
	WITHDRAWING("Extrayendo"),
	CANCELED("cancelada"),
	REJECTED("rechazada"),
	COMPLETED("completada"),
	NEW("nueva"), 
	PENDING_CONFIRMATION("Pendiente Confirmacion");
	
	public String status;
	
	private TransactionStatusEnum(String status) {
		
		this.status = status;
	}
}
