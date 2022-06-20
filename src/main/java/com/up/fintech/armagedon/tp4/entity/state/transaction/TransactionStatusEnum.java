package com.up.fintech.armagedon.tp4.entity.state.transaction;

public enum TransactionStatusEnum {

	SENDING("Enviando"),
	RECEIVING("Recibiendo"),
	RECEIVING_WITH_CONFIRMATION("Recibiendo con Confirmación"),
	DEPOSITING("Depositando"),
	WITHDRAWING("Extrayendo"),
	CANCELLED("Cancelada"),
	REJECTED("Rechazada"),
	COMPLETED("Completada"),
	NEW("Nueva"), 
	PENDING_CONFIRMATION("Pendiente de Confirmacion"),
	PREVIEW("Previsualización"),
	CHARGINGFEE("Cobrando fee"),
	PAYINGFEE("Pagando fee"),
	INVALIDSTATE("Estado invalido - revisar"), 
	GAMBLING("Apostando"), OPEN("Apuesta abierta"), CLOSED("Apuesta cerrada"), WIN("Ganó apuesta"), LOSE("Perdió Apuesta"), PAYINGWINNER("Pagando al ganador");
	
	public String status;
	
	private TransactionStatusEnum(String status) {
		
		this.status = status;
	}
}
