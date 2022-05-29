package com.up.fintech.armagedon.tp4.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.up.fintech.armagedon.tp4.entity.Deposit;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.misc.assembler.TransactionAssembler;
import com.up.fintech.armagedon.tp4.service.WalletService;

@RestController
@RequestMapping("/fintech/wallet/{wallet}/cash")
public class CashController {

	private final WalletService walletService;
	private final TransactionAssembler assembler;
	
	@Autowired
	public CashController(TransactionAssembler assembler, WalletService walletService) {
		this.walletService = walletService;
		this.assembler = assembler;
	}
	
	@PostMapping("/deposit")
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Transaction>>> depositMoney(@PathVariable UUID wallet, @RequestBody Deposit deposit) {
		var savedDeposit = walletService.getWallet(wallet).execute(deposit);
		var model = assembler.toModel(savedDeposit);
		var response = new ResponseStatusWrapper<>(model,true,0,"Deposit completed");
		return ResponseEntity.created(null).body(response);
	}
	
	public Transaction withdrawMoney() {
		return null;
	}
}
