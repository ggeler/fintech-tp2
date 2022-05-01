package com.up.fintech.armagedon.tp3.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@lombok.Data
public class SyncSecretKey implements Serializable, Cloneable {

	private static final long serialVersionUID = 7659833599775402032L;
	private String id;
	
//	@JsonIgnore
	private String rsaPrivateKey;

	private String secretKey;
	private String signature;
	private String digest;
	private String iv;
	private LocalDateTime expirationTime;
	private Instant instant;
	private Instant exp;
	
	@Override
	public SyncSecretKey clone() throws CloneNotSupportedException {
		return (SyncSecretKey) super.clone();
	}
}
