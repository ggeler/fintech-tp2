package com.up.fintech.armagedon.tp2.tp2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.up.fintech.armagedon.tp2.tp2.entity.UserTrail;
import com.up.fintech.armagedon.tp2.tp2.misc.assembler.UserTrailAssembler;
import com.up.fintech.armagedon.tp2.tp2.service.UserTrailService;

@RestController
@RequestMapping("/fintech/logs")
public class LogController {
	
	private final UserTrailService service;
	private final UserTrailAssembler assembler;
	
	@Autowired
	public LogController(UserTrailService service, UserTrailAssembler assembler) {
		this.service = service;
		this.assembler = assembler;
	}
	
	@GetMapping("")
	public ResponseEntity<List<UserTrail>> getLog() {
		return ResponseEntity.ok(service.getLog());
	}
	
	@GetMapping("/paged")
	public ResponseEntity<PagedModel<EntityModel<UserTrail>>> getLogPaged(Pageable pageable) {
		return ResponseEntity.ok(assembler.toModel(service.getLogPaged(pageable)));
	}
	
}
