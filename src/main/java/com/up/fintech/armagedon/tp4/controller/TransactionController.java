package com.up.fintech.armagedon.tp4.controller;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.up.fintech.armagedon.tp4.entity.EmptyResponse;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.service.TransactionService;

@RestController
@RequestMapping("/fintech/wallet/{wallet}/transactions")
public class TransactionController {
	
	private final TransactionService service;
	
	public TransactionController(TransactionService service) {
		this.service = service;
	}
	
	@GetMapping()
	public ResponseEntity<?> getTransactions(@PathVariable @NotNull UUID wallet) {
		try {
			var transactions = service.getTransactions(wallet);
			return ResponseEntity.ok().body(transactions);
		} catch (UserNotFoundException | WalletNotFoundException e) {
			var response = new ResponseStatusWrapper<>(new EmptyResponse(),true,HttpStatus.NOT_FOUND.value(),e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}
	
	@GetMapping("/paged")
	public ResponseEntity<?> getTransactionsPaged(@PathVariable @NotNull UUID wallet, Pageable pageable) {
		try {
			var transactions = service.getTransactions(wallet, pageable);
			return ResponseEntity.ok().body(transactions);
		} catch (UserNotFoundException | WalletNotFoundException e) {
			var response = new ResponseStatusWrapper<>(new EmptyResponse(),true,HttpStatus.NOT_FOUND.value(),e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}
	

}
