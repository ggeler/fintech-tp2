package com.up.fintech.armagedon.tp4.misc.state;

public enum TransactionStatus {

	SENDING("enviando"),
	RECEIVING("recibiendo"),
	DEPOSITING("Depositando"),
	WITHDRAWING("Extrayendo"),
	CANCEL("cancelada"),
	REJECTED("rechazada"),
	COMPLETED("completada"),
	NEW("nueva");
	
	public String status;
	
	private TransactionStatus(String status) {
		
		this.status = status;
	}
}
