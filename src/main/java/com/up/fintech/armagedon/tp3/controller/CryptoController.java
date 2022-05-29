package com.up.fintech.armagedon.tp3.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.up.fintech.armagedon.tp3.entity.RsaPublicKey;
import com.up.fintech.armagedon.tp3.entity.SyncSecretKey;
import com.up.fintech.armagedon.tp3.service.CryptoAesService;
import com.up.fintech.armagedon.tp3.service.CryptoRsaService;

@RestController
@RequestMapping("/fintech/crypto")
public class CryptoController {
	
	private final CryptoRsaService cryptoAsyncService;
	private final CryptoAesService cryptoSyncService;
	
	@Autowired
	public CryptoController(CryptoRsaService cryptoAsyncService, CryptoAesService cryptoSyncService ) {
		this.cryptoAsyncService = cryptoAsyncService;
		this.cryptoSyncService = cryptoSyncService;
	}

	@GetMapping("/publickey")
	public ResponseEntity<RsaPublicKey> getPublicKey()  {
		var publicKey = cryptoAsyncService.getPublicKey();
		return ResponseEntity.ok(publicKey);
	}
	
	@GetMapping("/secretkey")
	public ResponseEntity<SyncSecretKey> getSecretKey(@RequestParam(required = true) String id)  {
		SyncSecretKey secret;
		//id = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			secret = cryptoSyncService.assignAesSecretKey(id);
		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException | SignatureException | IOException | CloneNotSupportedException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return ResponseEntity.ok(secret);
	}		
	
	
	@GetMapping("/decrypt")
	public ResponseEntity<String> decrypt(@RequestParam String id, @RequestParam String encryptedTxt)  {
		
//		Transaction transaction = new Transaction();
//		transaction.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
//		transaction.setValue("Valores encryptados");
		
		String text;
		try {
			text = cryptoSyncService.decrypt(id, encryptedTxt);
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
	public ResponseEntity<String> encrypt(@RequestParam String id, @RequestParam String clearTxt)  {
		
//		Transaction transaction = new Transaction();
//		transaction.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
//		transaction.setValue("Valores encryptados");
		
		String text;
		try {
			text = cryptoSyncService.encrypt(id, clearTxt);
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
