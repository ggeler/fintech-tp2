package com.up.fintech.armagedon.tp4.controller;

import java.awt.image.BufferedImage;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;
import com.up.fintech.armagedon.tp4.controller.assembler.TransactionAssembler;
import com.up.fintech.armagedon.tp4.dto.ExternalTransferDto;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;
import com.up.fintech.armagedon.tp4.service.TransactionService;
import com.up.fintech.armagedon.tp4.service.WalletService;

@RestController
@RequestMapping("/fintech/wallet/{wallet}/cash/withdraw")
public class WithdrawController {

	private final WalletService walletService;
	private final TransactionService transactionService;
	private final TransactionAssembler assembler;
	
	@Autowired
	public WithdrawController(TransactionAssembler assembler, WalletService walletService, TransactionService transactionService) {
		this.walletService = walletService;
		this.transactionService = transactionService;
		this.assembler = assembler;
	}
	
	
	@PostMapping("")
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Transaction>>> withdraw(@PathVariable UUID wallet, @RequestBody Withdraw withdraw) {
		var savedWithdraw = walletService.execute(wallet, withdraw);
		var model = assembler.toModel(savedWithdraw);
		var response = new ResponseStatusWrapper<>(model,true,0,"Solicitud de retiro ingresada correctamente");
		return ResponseEntity.created(null).body(response);
	}
	
	@DeleteMapping("/{transaction}/cancel")
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Transaction>>> cancelWithdraw(@PathVariable UUID wallet, @PathVariable UUID transaction, @RequestBody ExternalTransferDto confirm) {
		var withdraw = transactionService.cancel(wallet, transaction, confirm);
		var model = assembler.toModel(withdraw);
		var response = new ResponseStatusWrapper<>(model,true,0,"Retiro cancelado");
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping("/{transaction}/confirm")
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Transaction>>> confirmWithdraw(@PathVariable UUID wallet, @PathVariable UUID transaction, @RequestBody ExternalTransferDto confirm) {
		var withdraw = transactionService.confirm(wallet, transaction, confirm);
		var model = assembler.toModel(withdraw);
		var response = new ResponseStatusWrapper<>(model,true,0,"Retiro confirmado");
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(value = "/{transaction}/qr", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<BufferedImage> getQrWithdraw(@PathVariable UUID wallet, @PathVariable UUID transaction) {
		BufferedImage qr;
		try {
			qr = transactionService.getQr(wallet, transaction);
			return ResponseEntity.ok().body(qr);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
