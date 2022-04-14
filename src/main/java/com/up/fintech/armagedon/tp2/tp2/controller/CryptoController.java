package com.up.fintech.armagedon.tp2.tp2.controller;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateExpiredException;
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

import com.up.fintech.armagedon.tp2.tp2.entity.RsaPublicKey;
import com.up.fintech.armagedon.tp2.tp2.entity.Transaction;
import com.up.fintech.armagedon.tp2.tp2.service.CryptoAsyncService;
import com.up.fintech.armagedon.tp2.tp2.service.CryptoSyncService;
import com.up.fintech.armagedon.tp2.tp2.entity.SyncSecretKey;

@RestController
@RequestMapping("/fintech/crypto")
public class CryptoController {
	
	private final CryptoAsyncService cryptoAsyncService;
	private final CryptoSyncService cryptoSyncService;
	
	@Autowired
	public CryptoController(CryptoAsyncService cryptoAsyncService, CryptoSyncService cryptoSyncService ) {
		this.cryptoAsyncService = cryptoAsyncService;
		this.cryptoSyncService = cryptoSyncService;
	}

	@GetMapping("/publickey")
	public ResponseEntity<RsaPublicKey> getPublicKey()  {
		var publicKey = cryptoAsyncService.getPublicKey();
		return ResponseEntity.ok(publicKey);
	}
	
	@GetMapping("/secretkey")
	public ResponseEntity<SyncSecretKey> getSecretKey(@RequestParam(required = false) String id)  {
		SyncSecretKey secret;
		id = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			secret = cryptoSyncService.assignAesSecretKey(id);
		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException | SignatureException | IOException | CloneNotSupportedException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return ResponseEntity.ok(secret);
	}		
	
	
	@GetMapping("/decrypt")
	public ResponseEntity<String> decrypt(@RequestParam String msg)  {
		
		Transaction transaction = new Transaction();
		transaction.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
		transaction.setValue("Valores encryptados");
		
		String text;
		try {
			text = cryptoSyncService.decrypt(msg);
		} catch ( NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException  e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (InvalidKeyException | CertificateExpiredException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return ResponseEntity.ok(text);
	}
	
	@GetMapping("/encrypt")
	public ResponseEntity<String> encrypt(@RequestParam String msg)  {
		
		Transaction transaction = new Transaction();
		transaction.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
		transaction.setValue("Valores encryptados");
		
		String text;
		try {
			text = cryptoSyncService.encrypt(msg);
		} catch ( NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException  e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (InvalidKeyException | CertificateExpiredException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return ResponseEntity.ok(text);
	}
		
		
//		try {
//			var user = cryptoService.assignAesSecretKey();
//			
//			var encryptedPublic = cryptoService.encryptWithPublicKey(transaction.getValue(), user.getRsaPublicKey());
//			var encryptedPublic2 = cryptoService.encryptWithPublicKey(transaction.getValue(), user.getRsaPublicKey());
//			var encryptedPrivate = cryptoService.encryptWithPrivateKey(transaction.getValue(), user.getRsaPrivateKey());
//			
//			var decryptedPublic = cryptoService.decryptWithPrivaterKey(encryptedPublic, user.getRsaPrivateKey());
//			var decryptedPrivate = cryptoService.decryptWithPublicKey(encryptedPrivate, user.getRsaPublicKey());
//			
//			var sign = cryptoService.sign(transaction.getValue(), user.getRsaPrivateKey());
//			var verify = cryptoService.verifySignature(sign, transaction.getValue()+"1", user.getRsaPublicKey());
//			
//			System.out.println(decryptedPrivate);
//			System.out.println(decryptedPublic);
//			
//		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException | SignatureException e) {
//			e.printStackTrace();
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
//		}
//		
}
