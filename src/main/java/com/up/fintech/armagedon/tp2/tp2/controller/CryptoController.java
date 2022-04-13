package com.up.fintech.armagedon.tp2.tp2.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.up.fintech.armagedon.tp2.tp2.entity.Transaction;
import com.up.fintech.armagedon.tp2.tp2.entity.User;
import com.up.fintech.armagedon.tp2.tp2.misc.encryption.CryptoService;

@RestController
@RequestMapping("/fintech/crypto")
public class CryptoController {
	
	private final CryptoService cryptoService;
	
	@Autowired
	public CryptoController(CryptoService cryptoService ) {
		this.cryptoService = cryptoService;
	}

	@GetMapping("/publickey")
	public ResponseEntity<User> getPublicKey()  {
		
		User user;
		try {
			user = cryptoService.assignRsaKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
		return ResponseEntity.ok(user);
		
	}	
	
	@GetMapping("/encrypt")
	public ResponseEntity<User> encrypt()  {
		
		Transaction transaction = new Transaction();
		transaction.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
		transaction.setValue("Valores encryptados");
		
		try {
			var user = cryptoService.assignRsaKeyPair();
			
			var encryptedPublic = cryptoService.encryptWithPublicKey(transaction.getValue(), user.getRsaPublicKey());
			var encryptedPrivate = cryptoService.encryptWithPrivateKey(transaction.getValue(), user.getRsaPrivateKey());
			
			var decryptedPublic = cryptoService.decryptWithPrivaterKey(encryptedPublic, user.getRsaPrivateKey());
			var decryptedPrivate = cryptoService.decryptWithPublicKey(encryptedPrivate, user.getRsaPublicKey());
			
			var sign = cryptoService.sign(transaction.getValue(), user.getRsaPrivateKey());
			var verify = cryptoService.verifySignature(sign, transaction.getValue()+"1", user.getRsaPublicKey());
			
			System.out.println(decryptedPrivate);
			System.out.println(decryptedPublic);
			
		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException | SignatureException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
		return ResponseEntity.ok(new User());
		
	}
}
