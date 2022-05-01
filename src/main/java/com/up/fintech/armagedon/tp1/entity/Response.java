package com.up.fintech.armagedon.tp1.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.up.fintech.armagedon.tp2.entity.Data;

import lombok.AccessLevel;
import lombok.Setter;

@lombok.Data

public class Response {

	private Boolean status;
	@JsonIgnore
	@Setter(value = AccessLevel.NONE)
	private Integer internalErrorCode;
	@Setter(value = AccessLevel.NONE)
	private String errorCode;
	private String msg;
	private Data data;
	private UUID transactionId;
	private LocalDateTime timeStamp;
	
	public void setInternalErrorCode(Integer code) {
		internalErrorCode = code;
		if (internalErrorCode == 0) {
			errorCode = "00";
		} else
			errorCode = String.valueOf(code);
	}
	
}
