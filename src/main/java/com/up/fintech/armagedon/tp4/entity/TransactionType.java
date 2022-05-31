package com.up.fintech.armagedon.tp4.entity;

public enum TransactionType {

	DEPOSIT("Deposito"),
	WITHDRAW("Retiro"),
	INTERNAL_SEND("Transferencia Interna"),
	INTERNAL_RECEIVE("Recibo Interno"),
	EXTERNAL_SEND("Transferencia Externa"),
	EXTERNAL_RECEIVE("Recibo Externo"),
	EXTERNAL_RECEIVE_WITHCONFIRM("Recibo externo con confirmación");
	
	public final String value;
	
	private TransactionType(String value) {
		this.value = value;
	}
}
