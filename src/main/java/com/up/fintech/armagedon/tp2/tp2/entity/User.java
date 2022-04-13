package com.up.fintech.armagedon.tp2.tp2.entity;

@lombok.Data
public class User {

	private String id;
	private String username;
	
	private String rsaPublicKey;
	private String rsaPrivateKey;
	
	private String aesKey;
}
