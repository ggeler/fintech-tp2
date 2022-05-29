package com.up.fintech.armagedon.tp4.controller;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.misc.assembler.TransactionAssembler;
import com.up.fintech.armagedon.tp4.service.TransactionService;

@RestController
@RequestMapping("/fintech/wallet/{wallet}/transactions")
public class TransactionController {
	
	private final TransactionService service;
	private final TransactionAssembler assembler;
	
	@Autowired
	public TransactionController(TransactionService service, TransactionAssembler assembler) {
		this.service = service;
		this.assembler = assembler;
	}

	@GetMapping()
	public ResponseEntity<PagedModel<EntityModel<Transaction>>> getTransactionsPaged(@PathVariable @NotNull UUID wallet, Pageable pageable) {
		var transactions = service.getTransactions(wallet, pageable);
		return ResponseEntity.ok().body(assembler.toModel(transactions));
	}


}
