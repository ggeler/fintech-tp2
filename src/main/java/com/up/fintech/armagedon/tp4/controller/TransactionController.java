package com.up.fintech.armagedon.tp4.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.up.fintech.armagedon.tp4.controller.assembler.TransactionAssembler;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.misc.component.Views;
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

	@GetMapping() //@JsonView(Views.Public.class) 
	public ResponseEntity<PagedModel<EntityModel<EntityModel<Transaction>>>> getTransactionsPaged(@PathVariable @NotNull UUID wallet, 
			@SortDefault(sort = "timestamp", direction = Direction.DESC) Pageable pageable) {
		var transactions = service.getTransactions(wallet, pageable);
		return ResponseEntity.ok().body(assembler.toModel(transactions.map(assembler::toModel)));
	}
	
	@GetMapping("/{transaction}") //@JsonView(Views.Public.class) 
	public ResponseEntity<EntityModel<Transaction>> getTransaction(@PathVariable @NotNull UUID wallet, @PathVariable @NotNull UUID transaction) {
		var tx = service.getTransaction(wallet, transaction);
		return ResponseEntity.ok().body(assembler.toModel(tx));
	}
	
	@GetMapping("/test") @JsonView(Views.Internal.class) 
	public List<Transaction> getTransactions(@PathVariable @NotNull UUID wallet) {
		return service.getTransactions(wallet);
	}

}
