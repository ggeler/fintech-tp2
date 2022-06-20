package com.up.fintech.armagedon.tp4.entity;

public enum TransactionType {

	DEPOSIT("Deposito por Ventanilla"),
	WITHDRAW("Retiro por Ventanilla"),
	INTERNAL_SEND("Transferencia Billetera misma Compañía"),
	INTERNAL_RECEIVE("Recibo desde Billetera misma Compañía"),
	EXTERNAL_SEND("Transferencia a Entidad Externa"),
	EXTERNAL_RECEIVE("Recibo desde Entidad Externa"),
	EXTERNAL_RECEIVE_WITHCONFIRM("Recibo externo con confirmación"),
	FEECHARGE("Cargos por Comisión"),
	FEEPAY("Cobro por Comisión"), 
	BET("Ingreso Apuesta"),
	BETBAG("Recaudación por apuesta");
	
	public final String value;
	
	private TransactionType(String value) {
		this.value = value;
	}
}
