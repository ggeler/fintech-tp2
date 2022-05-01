package com.up.fintech.armagedon.tp4.entity;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class ResponseStatusWrapper<T> {
	
	private T data;
	
	private Boolean status;
	@JsonIgnore
	@Setter(value = AccessLevel.NONE)
	private Integer internalErrorCode;
	@Setter(value = AccessLevel.NONE)
	private String errorCode;
	private String msg;
	private UUID transactionId = UUID.randomUUID();
	private Instant timeStamp = Instant.now();
	
	public ResponseStatusWrapper(T data, boolean status, int errorCode, String msg) {
		if (data!=null) 
			this.data = data;
		this.status = status;
		setInternalErrorCode(errorCode);
		this.msg = msg;		
	}
	
	public void setInternalErrorCode(Integer code) {
		internalErrorCode = code;
		if (internalErrorCode == 0) {
			errorCode = "00";
		} else
			errorCode = String.valueOf(code);
	}
}
