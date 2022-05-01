package com.up.fintech.armagedon.tp4.misc.error;

public class WalletAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -3686138223383686922L;

	public WalletAlreadyExistsException(String msg) {
		super(msg);
	}
}
