package com.up.fintech.armagedon.tp2.tp2.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp2.tp2.dto.Data;
import com.up.fintech.armagedon.tp2.tp2.dto.Request;
import com.up.fintech.armagedon.tp2.tp2.dto.Response;

@Service
public class EchoService {

	public Response echoReply(Request request) {
		
		var response = new Response();
		response.setStatus(true);
		response.setErrorCode(0);
		response.setMsg("Estado operación");
		response.setData(new Data());
		response.setTimeStamp(LocalDateTime.now());
		response.setTransactionId(UUID.randomUUID());
		
		return response;
	}
	
}
