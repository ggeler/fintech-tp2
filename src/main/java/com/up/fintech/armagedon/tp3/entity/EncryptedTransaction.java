package com.up.fintech.armagedon.tp3.entity;

import lombok.Data;

@Data
public class EncryptedTransaction {

	private String userId;
	private String payload;
	private String encAesKey;
}
