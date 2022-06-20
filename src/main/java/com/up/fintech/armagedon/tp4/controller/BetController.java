package com.up.fintech.armagedon.tp4.controller;

import java.util.UUID;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.up.fintech.armagedon.tp4.controller.assembler.TransactionAssembler;
import com.up.fintech.armagedon.tp4.dto.ExternalTransferDto;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.service.TransactionService;
import com.up.fintech.armagedon.tp4.service.WalletService;

@RestController
@RequestMapping("/fintech/wallet/{wallet}/bet")
public class BetController {
	
	private final WalletService walletService;
	private final TransactionAssembler assembler;
	private final TransactionService transactionService;
	
	public BetController(WalletService walletService, TransactionAssembler assembler, TransactionService transactionService) {
		this.walletService = walletService;
		this.assembler = assembler;
		this.transactionService = transactionService;
	}
	
	@GetMapping("")
	public ResponseEntity<ResponseStatusWrapper<Transaction>> preview(@PathVariable UUID wallet, @RequestBody Bet bet) {
		var preview = walletService.preview(wallet, bet);
		var response = new ResponseStatusWrapper<>(preview,true,0,"Previsualización de Apuesta");
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("")
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Transaction>>> request(@PathVariable UUID wallet, @RequestBody Bet bet) {
		var request = walletService.execute(wallet, bet);
		var model = assembler.toModel(request);
		var response = new ResponseStatusWrapper<>(model,true,0,"Previsualización de Apuesta");
		return ResponseEntity.created(null).body(response);
	}
	
	@PutMapping("/{transaction}/confirm")
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Transaction>>> confirm(@PathVariable UUID wallet, @PathVariable UUID transaction, @RequestParam String confirm) {
		var request = transactionService.confirm(wallet, transaction, new ExternalTransferDto(confirm));
		var model = assembler.toModel(request);
		var response = new ResponseStatusWrapper<>(model,true,0,"Previsualización de Apuesta");
		return ResponseEntity.created(null).body(response);
	}
	
	@DeleteMapping("/{transaction}/cancel")
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Transaction>>> cancel(@PathVariable UUID wallet, @PathVariable UUID transaction, @RequestParam String confirm) {
		var withdraw = transactionService.cancel(wallet, transaction, new ExternalTransferDto(confirm));
		var model = assembler.toModel(withdraw);
		var response = new ResponseStatusWrapper<>(model,true,0,"Retiro cancelado");
		return ResponseEntity.ok().body(response);
	}
}