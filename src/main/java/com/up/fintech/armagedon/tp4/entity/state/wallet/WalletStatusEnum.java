package com.up.fintech.armagedon.tp4.entity.state.wallet;

public enum WalletStatusEnum {

	ENABLED("Habilitada"),
	BLOCKED("Bloqueada"),
	BLOCKED_SEND("Bloqueada para Transferir"),
	BLOCKED_RECEIVE("Bloqueada para Recibir/Depositar"),
	CLOSED("Cerrada");
	
	public String status;
	
	private WalletStatusEnum(String status) {
		
		this.status = status;
	}
}
