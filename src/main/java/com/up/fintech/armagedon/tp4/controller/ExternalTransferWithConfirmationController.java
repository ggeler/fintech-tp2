package com.up.fintech.armagedon.tp4.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.up.fintech.armagedon.tp4.controller.assembler.ExternalReceiveTransferWithConfirmationAssembler;
import com.up.fintech.armagedon.tp4.dto.ExternalTransferDto;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.credit.ExternalIn;
import com.up.fintech.armagedon.tp4.misc.component.SpringContext;
import com.up.fintech.armagedon.tp4.misc.component.Views;
import com.up.fintech.armagedon.tp4.service.TransactionService;
import com.up.fintech.armagedon.tp4.service.WalletService;
import com.up.fintech.armagedon.tp4.strategy.ExternalReceiveTransferWithConfirmationServiceStrategy;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/fintech/wallet/{wallet}/external/transfer")
@Log4j2
public class ExternalTransferWithConfirmationController {

	private final WalletService walletService;
	private final TransactionService transactionService;
	private final ExternalReceiveTransferWithConfirmationAssembler assembler;
	
	@Autowired
	public ExternalTransferWithConfirmationController(WalletService walletService, ExternalReceiveTransferWithConfirmationAssembler assembler, TransactionService transactionService) {
		this.walletService = walletService;
		this.transactionService = transactionService;
		this.assembler = assembler;
	}
	
	@PostMapping
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Transaction>>> receiveTransfer(@PathVariable UUID wallet, @RequestBody  @JsonView(Views.Public.class) ExternalIn externalTransfer) {
		log.info("External bank recieve transfer");
		log.info("Request amount: "+externalTransfer.getAmount()+" - from:"+externalTransfer.getFromCvu()+" to:"+externalTransfer.getToCvu());
		externalTransfer.setStrategy(SpringContext.getBean(ExternalReceiveTransferWithConfirmationServiceStrategy.class));
		var transfer = walletService.execute(externalTransfer.getToCvu(), externalTransfer);
		var model = assembler.toModel(transfer);
		var response = new ResponseStatusWrapper<>(model,true,0,"Transferencia pendiente confirmación/cancelación");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping("/{transaction}/confirm")
	public ResponseEntity<ResponseStatusWrapper<Transaction>> confirmTransfer(@PathVariable UUID wallet, @PathVariable UUID transaction, @RequestBody ExternalTransferDto confirm) {
		var transfer = transactionService.confirm(wallet, transaction, confirm);
		var response = new ResponseStatusWrapper<>(transfer,true,0,"Transferencia confirmada correctamente");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping("/{transaction}/cancel")
	public ResponseEntity<ResponseStatusWrapper<Transaction>>  cancelTransfer(@PathVariable UUID wallet, @PathVariable UUID transaction, @RequestBody ExternalTransferDto confirm) {
		var transfer = transactionService.cancel(wallet, transaction, confirm);
		var response = new ResponseStatusWrapper<>(transfer,true,0,"Transferencia cancelada correctamente");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
