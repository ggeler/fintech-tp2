package com.up.fintech.armagedon.tp4.controller;

import java.util.UUID;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.up.fintech.armagedon.tp4.controller.assembler.TransactionAssembler;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.debit.InternalOut;
import com.up.fintech.armagedon.tp4.service.WalletService;

@RestController
@RequestMapping("/fintech/wallet/{wallet}/transfer")
public class TransferController {
	
	private final WalletService walletService;
	private final TransactionAssembler assembler;
	
	public TransferController(TransactionAssembler assembler, WalletService walletService) {
		this.walletService = walletService;
		this.assembler = assembler;
	}
	
	@PostMapping 
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Transaction>>> transfer(@PathVariable UUID wallet, @RequestBody InternalOut transfer) {
		var internalTransfer = walletService.execute(wallet, transfer);
		var model = assembler.toModel(internalTransfer);
		var response = new ResponseStatusWrapper<>(model,true,0,"Transfer completed");
		return ResponseEntity.created(null).body(response);
	}
}
