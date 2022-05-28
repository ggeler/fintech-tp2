package com.up.fintech.armagedon.tp4.misc.error;

public class WalletNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8154435745365810961L;

	public WalletNotFoundException(String msg) {
		super(msg);
	}
}
