package com.up.fintech.armagedon.tp2.tp2.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.up.fintech.armagedon.tp2.tp2.entity.Iso8583;
import com.up.fintech.armagedon.tp2.tp2.entity.Request;
import com.up.fintech.armagedon.tp2.tp2.entity.Response;
import com.up.fintech.armagedon.tp2.tp2.entity.UserTrail;
import com.up.fintech.armagedon.tp2.tp2.misc.assembler.UserTrailAssembler;
import com.up.fintech.armagedon.tp2.tp2.service.Decoder8583Service;
import com.up.fintech.armagedon.tp2.tp2.service.EchoService;
import com.up.fintech.armagedon.tp2.tp2.service.UserTrailService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/fintech/echo/test")
@Log4j2
public class EchoTestController {

	private final EchoService echoService;
	private final Decoder8583Service decoder;
	private final UserTrailService service;
	private final UserTrailAssembler assembler;
	
	@Autowired
	public EchoTestController(EchoService echoService, Decoder8583Service decoder, UserTrailService service, UserTrailAssembler assembler) {
		this.echoService = echoService;
		this.decoder = decoder;
		this.service = service;
		this.assembler = assembler;
	}
	
	@GetMapping("/me")
	public ResponseEntity<Principal> getMe(Principal principal) {
		return ResponseEntity.ok(principal);
	}
	
	@GetMapping("/log")
	public ResponseEntity<List<UserTrail>> getLog() {
		return ResponseEntity.ok(service.getLog());
	}
	
	@GetMapping("/log/paged")
	public ResponseEntity<PagedModel<EntityModel<UserTrail>>> getLogPaged(Pageable pageable) {
		return ResponseEntity.ok(assembler.toModel(service.getLogPaged(pageable)));
	}
	
	@PostMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<Response> json(@RequestBody Request request) {
		
		var principal = SecurityContextHolder.getContext().getAuthentication().getName();
		
		service.log(new UserTrail(null, principal, LocalDateTime.now(), "POST /fintech/echo/test/json"));
		log.info("Accediendo a /json");
		
		var response = echoService.echoReply(request);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "/iso8583", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.ALL_VALUE)
	public ResponseEntity<String> iso8583(@RequestParam(required=true) String param, @RequestParam (required = false) String request) {
		
		var principal = SecurityContextHolder.getContext().getAuthentication().getName();
		
		service.log(new UserTrail(null, principal, LocalDateTime.now(), "POST /fintech/echo/test/iso8583"));
		log.info("Accediendo a /iso8583");
		
		Iso8583 response = null;
		if (request == null || request.isBlank())
			request = "FFFFFFFFFFFFFFFF"; //3238050020C1801C
		
		try {
			response = decoder.converter(request);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		
		if (param!=null && param.equals("bits"))
			return ResponseEntity.ok(response.getBits());
		
		else if (param!=null && param.equals("functions"))
			return ResponseEntity.ok(response.getFunctions());
		
		else 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parametros soportados ?param=functions o ?param=bits");
	}
	
}
