package com.up.fintech.armagedon.tp2.tp2.dto;

import java.time.LocalDateTime;
import java.util.UUID;

@lombok.Data
public class Response {

	private Boolean status;
	private Integer errorCode;
	private String msg;
	private Data data;
	private UUID transactionId;
	private LocalDateTime timeStamp;
	
}
