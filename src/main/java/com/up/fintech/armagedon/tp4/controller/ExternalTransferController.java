package com.up.fintech.armagedon.tp4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.up.fintech.armagedon.tp4.entity.ExternalReceiveTransfer;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.misc.component.Views;
import com.up.fintech.armagedon.tp4.service.WalletService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/fintech/external/bank/transfer")
@Log4j2
public class ExternalTransferController {

	private final WalletService walletService;
	
	@Autowired
	public ExternalTransferController(WalletService walletService) {
		this.walletService = walletService;
	}
	
	@PostMapping
	public ResponseEntity<ResponseStatusWrapper<Transaction>> receiveTransfer(@RequestBody  @JsonView( Views.Public.class) ExternalReceiveTransfer externalTransfer) {
		log.info("External bank recieve transfer");
		log.info("Request amount: "+externalTransfer.getAmount()+" - from:"+externalTransfer.getFromCvu()+" to:"+externalTransfer.getToCvu());
		var transfer = walletService.getWallet(externalTransfer.getToCvu()).execute(externalTransfer);
		var response = new ResponseStatusWrapper<>(transfer,true,0,"Transfer completed");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	public void confirmTransfer() {
		
	}
}
