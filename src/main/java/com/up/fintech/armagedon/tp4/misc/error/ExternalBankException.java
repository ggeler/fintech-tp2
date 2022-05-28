package com.up.fintech.armagedon.tp4.misc.error;

public class ExternalBankException extends RuntimeException {

	private static final long serialVersionUID = 295324887970776252L;

	public ExternalBankException(String msg) {
		super(msg);
	}
}
