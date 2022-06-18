package com.up.fintech.armagedon.tp4.entity.state.transaction;

public enum TransactionStatusEnum {

	SENDING("Enviando"),
	RECEIVING("Recibiendo"),
	RECEIVING_WITH_CONFIRMATION("Recibiendo con Confirmación"),
	DEPOSITING("Depositando"),
	WITHDRAWING("Extrayendo"),
	CANCELED("Cancelada"),
	REJECTED("Rechazada"),
	COMPLETED("Completada"),
	NEW("Nueva"), 
	PENDING_CONFIRMATION("Pendiente de Confirmacion"),
	PREVIEW("Previsualización"),
	CHARGINGFEE("Cobrando fee"),
	PAYINGFEE("Pagando fee");
	
	public String status;
	
	private TransactionStatusEnum(String status) {
		
		this.status = status;
	}
}
