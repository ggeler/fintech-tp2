package com.up.fintech.armagedon.tp1.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

import com.up.fintech.armagedon.tp1.entity.Iso8583;
import com.up.fintech.armagedon.tp1.entity.Request;
import com.up.fintech.armagedon.tp1.entity.Response;
import com.up.fintech.armagedon.tp1.service.Decoder8583Service;
import com.up.fintech.armagedon.tp1.service.EchoService;
import com.up.fintech.armagedon.tp2.entity.UserTrail;
import com.up.fintech.armagedon.tp2.service.UserTrailService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/fintech/echo/test")
@Log4j2
public class EchoTestController {

	private final EchoService echoService;
	private final Decoder8583Service decoder;
	private final UserTrailService service;
	
	@Autowired
	public EchoTestController(EchoService echoService, Decoder8583Service decoder, UserTrailService service) {
		this.echoService = echoService;
		this.decoder = decoder;
		this.service = service;
	}
	
	
	static class CustomRepresentationModel extends RepresentationModel<CustomRepresentationModel> {
		public CustomRepresentationModel(Iterable<Link> initialLinks) {
			super(initialLinks);
		}
	}
	
	@GetMapping 
	public CustomRepresentationModel index() {
		
		List<Link> initialLinks = new ArrayList<>();
		
		initialLinks.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EchoTestController.class).index()).withSelfRel());
		initialLinks.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EchoTestController.class).getMe(null)).withRel("me"));
		initialLinks.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EchoTestController.class).iso8583(null,null)).withRel("iso8583"));
		initialLinks.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EchoTestController.class).json(null)).withRel("json"));
		
		return new CustomRepresentationModel(initialLinks);
	}
	
	@GetMapping("/me")
	public ResponseEntity<Principal> getMe(Principal principal) {
		return ResponseEntity.ok(principal);
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
	public ResponseEntity<String> iso8583(@RequestParam(defaultValue = "bits", required=true) String param, @RequestParam (name = "request", defaultValue = "FFFFFFFFFFFFFFFF", required = false) String request) {
		
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
