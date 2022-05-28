package com.up.fintech.armagedon.tp4.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.up.fintech.armagedon.tp4.entity.EmptyResponse;
import com.up.fintech.armagedon.tp4.entity.InternalSendTransfer;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.misc.assembler.TransactionAssembler;
import com.up.fintech.armagedon.tp4.misc.error.CvuException;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;
import com.up.fintech.armagedon.tp4.service.TransferService;

@RestController
@RequestMapping("/fintech/wallet/{wallet}/transfer")
public class TransferController {
	
	private final TransferService service;
	private final TransactionAssembler assembler;
	
	public TransferController(TransferService service, TransactionAssembler assembler) {
		this.service = service;
		this.assembler = assembler;
	}
	
	@PostMapping
	public ResponseEntity<ResponseStatusWrapper<?>> transferMoney(@PathVariable UUID wallet, @RequestBody InternalSendTransfer transfer) {
		try {
			var internalTransfer = service.internalTransfer(wallet, transfer);
			var model = assembler.toModel(internalTransfer);
			var response = new ResponseStatusWrapper<>(model,true,0,"Transfer completed");
			return ResponseEntity.created(null).body(response);
		} catch (TransactionException e) {
			var response = new ResponseStatusWrapper<>(new EmptyResponse(),true,HttpStatus.BAD_REQUEST.value(),e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} catch (UserNotFoundException | WalletNotFoundException  | CvuException e) {
			var response = new ResponseStatusWrapper<>(new EmptyResponse(),true,HttpStatus.NOT_FOUND.value(),e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@GetMapping
	public InternalSendTransfer confirmTransfer() {
		return null;
	}
}
