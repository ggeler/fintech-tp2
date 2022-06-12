package com.up.fintech.armagedon.tp4.entity.state.wallet;

public enum WalletStatusEnum {

	ENABLED("Habilitada"),
	BLOCKED("Bloqueada"),
	BLOCKED_WITHDRAW("Bloqueada para Transferir/Retirar"),
	BLOCKED_DEPOSIT("Bloqueada para Recibir/Depositar"),
	CLOSED("Cerrada");
	
	public String status;
	
	private WalletStatusEnum(String status) {
		
		this.status = status;
	}
}
