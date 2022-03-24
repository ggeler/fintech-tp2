package com.up.fintech.armagedon.tp2.tp2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.up.fintech.armagedon.tp2.tp2.dto.Iso8583;
import com.up.fintech.armagedon.tp2.tp2.dto.Request;
import com.up.fintech.armagedon.tp2.tp2.dto.Response;
import com.up.fintech.armagedon.tp2.tp2.service.Decoder8583;
import com.up.fintech.armagedon.tp2.tp2.service.EchoService;

@RestController
@RequestMapping("/fintech/echo")
public class EchoController {

	private final EchoService echoService;
	private final Decoder8583 decoder;
	
	@Autowired
	public EchoController(EchoService echoService, Decoder8583 decoder) {
		this.echoService = echoService;
		this.decoder = decoder;
	}
	
	@PostMapping("/test")
	public ResponseEntity<Response> Test(@RequestBody Request request) {
		
		var response = echoService.echoReply(request);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/test8583")
	public ResponseEntity<Iso8583> Test(@RequestBody String request) {
		
		var response = decoder.converter(request);
		
		return ResponseEntity.ok(response);
	}
	
}
