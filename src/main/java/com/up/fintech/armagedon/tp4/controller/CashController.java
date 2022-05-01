package com.up.fintech.armagedon.tp4.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.up.fintech.armagedon.tp4.entity.Deposit;
import com.up.fintech.armagedon.tp4.entity.EmptyResponse;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.misc.assembler.TransactionAssembler;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.service.CashService;

@RestController
@RequestMapping("/fintech/wallet/{wallet}/cash")
public class CashController {

	private final CashService service;
	private final TransactionAssembler assembler;
	
	@Autowired
	public CashController(CashService service, TransactionAssembler assembler) {
		this.service = service;
		this.assembler = assembler;
	}
	
	@PostMapping("/deposit")
	public ResponseEntity<ResponseStatusWrapper<?>> depositMoney(@PathVariable UUID wallet, @RequestBody Deposit deposit) {
		try {
			var savedDeposit = service.deposit(wallet, deposit);
			var model = assembler.toModel(savedDeposit);
			var response = new ResponseStatusWrapper<>(model,true,0,"Deposit completed");
			return ResponseEntity.created(null).body(response);
		}
		catch (WalletNotFoundException | UserNotFoundException e) {
			var response = new ResponseStatusWrapper<>(new EmptyResponse(),true,HttpStatus.NOT_FOUND.value(),e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} catch (TransactionException e) {
			var response = new ResponseStatusWrapper<>(new EmptyResponse(),true,HttpStatus.BAD_REQUEST.value(),e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	public Transaction withdrawMoney() {
		return null;
	}
}
