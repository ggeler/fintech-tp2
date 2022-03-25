package com.up.fintech.armagedon.tp2.tp2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
	
	@PostMapping("/test/json")
	public ResponseEntity<Response> Test(@RequestBody Request request) {
		
		var response = echoService.echoReply(request);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/test/iso8583")
	public ResponseEntity<String> Test(@RequestParam(required=true) String param) {//, @RequestBody String request) {
		Iso8583 response = null;
		var request = "FFFFFFFFFFFFFFFF";
		try {
			response = decoder.converter(request);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		
		if (param!=null && param.equals("bits"))
			return ResponseEntity.ok(response.getBits());
		else if (param!=null && param.equals("functions"))
			return ResponseEntity.ok(response.getFunctions());
		else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Soportado ?param=functions o ?param=bits");
	}
	
}
