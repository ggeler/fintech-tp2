package com.up.fintech.armagedon.tp4.controller;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.entity.User;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.misc.assembler.WalletAssembler;
import com.up.fintech.armagedon.tp4.service.WalletService;


@RestController
@RequestMapping("/fintech/wallet")
public class WalletController {

	private final WalletService service;
	private final WalletAssembler assembler;

	@Autowired
	public WalletController(WalletService service, WalletAssembler assembler) {
		this.service = service;
		this.assembler = assembler;
	}

	@GetMapping()
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Wallet>>> getWalletByUser(@RequestParam UUID user) {
		var wallet = service.getWalletByUserUuid(user);
		var model = assembler.toModel(wallet);
		var response = new ResponseStatusWrapper<>(model, true, 0, "Wallet found");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{wallet}")
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Wallet>>> getWallet(@PathVariable UUID wallet) {
		var tmp = service.getWallet(wallet);
		var model = assembler.toModel(tmp);
		var response = new ResponseStatusWrapper<>(model, true, 0, "Wallet found");
		return ResponseEntity.ok(response);
	}

	@PostMapping @Valid
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Wallet>>> createWallet(@RequestParam @NotNull UUID user, @RequestParam @Email String email) {
		Wallet wallet;
		wallet = service.addWallet(new User(user,email));
		var model = assembler.toModel(wallet);
		var response = new ResponseStatusWrapper<>(model, true, 0, "Wallet Succesfully created");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
