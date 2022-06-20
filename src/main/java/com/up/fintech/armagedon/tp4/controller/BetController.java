package com.up.fintech.armagedon.tp4.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
import com.up.fintech.armagedon.tp4.entity.state.transaction.TransactionStatusEnum;
import com.up.fintech.armagedon.tp4.service.BetService;
import com.up.fintech.armagedon.tp4.service.TransactionService;
import com.up.fintech.armagedon.tp4.service.WalletService;

@RestController
@RequestMapping("/fintech/wallet/{wallet}/bet")
public class BetController {
	
	private final WalletService walletService;
	private final TransactionAssembler assembler;
	private final TransactionService transactionService;
	private final BetService betService;
	
	public BetController(WalletService walletService, TransactionAssembler assembler, TransactionService transactionService, BetService betService) {
		this.walletService = walletService;
		this.assembler = assembler;
		this.transactionService = transactionService;
		this.betService = betService;
	}
	
	@GetMapping("")
	public ResponseEntity<ResponseStatusWrapper<Transaction>> preview(@PathVariable UUID wallet, @RequestBody Bet bet) {
		var preview = walletService.preview(wallet, bet);
		var response = new ResponseStatusWrapper<>(preview,true,0,"Previsualización de Apuesta");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all")
	public ResponseEntity<PagedModel<EntityModel<EntityModel<Transaction>>>> allBets(@PathVariable UUID wallet, @RequestParam(required = false) TransactionStatusEnum status , Pageable pageable) {
		if (status == null) {
			var transactions = betService.getAllBets(wallet, pageable);
			return ResponseEntity.ok().body(assembler.toModel(transactions.map(assembler::toModel)));
		}
		else {
			var transactions = betService.getAllBets(wallet, status, pageable);
			return ResponseEntity.ok().body(assembler.toModel(transactions.map(assembler::toModel)));
		}
	}
	
//	@GetMapping("/all/open")
//	public ResponseEntity<PagedModel<EntityModel<EntityModel<Transaction>>>> allBetsOpen(@PathVariable UUID wallet, Pageable pageable) {
//		var transactions = betService.getAllBetsOpened(wallet, pageable);
//		return ResponseEntity.ok().body(assembler.toModel(transactions.map(assembler::toModel)));
//	}
//	
//	@GetMapping("/all/closed")
//	public ResponseEntity<PagedModel<EntityModel<EntityModel<Transaction>>>> allBetsClosed(@PathVariable UUID wallet, Pageable pageable) {
//		var transactions = betService.getAllBetsClosed(wallet, pageable);
//		return ResponseEntity.ok().body(assembler.toModel(transactions.map(assembler::toModel)));
//	}
//	
//	@GetMapping("/all/pending")
//	public ResponseEntity<PagedModel<EntityModel<EntityModel<Transaction>>>> allBetsPending(@PathVariable UUID wallet, Pageable pageable) {
//		var transactions = betService.getAllBetsPendingConfirmation(wallet, pageable);
//		return ResponseEntity.ok().body(assembler.toModel(transactions.map(assembler::toModel)));
//	}
//	
//	@GetMapping("/all/cancelled")
//	public ResponseEntity<PagedModel<EntityModel<EntityModel<Transaction>>>> allBetsCancelled(@PathVariable UUID wallet, Pageable pageable) {
//		var transactions = betService.getAllBetsCanceled(wallet, pageable);
//		return ResponseEntity.ok().body(assembler.toModel(transactions.map(assembler::toModel)));
//	}
	
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