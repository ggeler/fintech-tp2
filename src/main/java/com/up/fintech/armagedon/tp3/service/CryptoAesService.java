package com.up.fintech.armagedon.tp3.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateExpiredException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.up.fintech.armagedon.tp3.entity.SyncSecretKey;

public final class CryptoAesService {
	
	private final CryptoRsaService cryptoAsyncService;
	
	private static final String cipher = "AES/CBC/PKCS5Padding"; //"AES";
	private static final String alghoritm = "AES";
	private static final int keylenght = 128;
	private static final long ttl = 30L;
	
	private final IvParameterSpec iv = generateIv();
	private final ConcurrentHashMap<String, SyncSecretKey> secretKeys = new ConcurrentHashMap<>();
	
	private static CryptoAesService instance = null;
	
	private CryptoAesService(CryptoRsaService cryptoAsyncService) {
		this.cryptoAsyncService = cryptoAsyncService;
	}

	public synchronized static CryptoAesService getInstance(CryptoRsaService asyncService)   {
		if (instance == null) {
			instance = new CryptoAesService(asyncService);
		}
		return instance;
	}
	
	public SyncSecretKey assignAesSecretKey(@NotBlank String id) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, SignatureException, IOException, CloneNotSupportedException   {
		
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var name = authentication.getName();
		
		Assert.isTrue(name.equals(id),"name not match");
		
		if (secretKeys.containsKey(name)) {
			return secretKeys.get(name);
		} else {
			
			var secretKey = generateAESKey(keylenght);
			var encodedSecret = new String(Base64.getEncoder().encode(secretKey.getEncoded()));
//			var encodedSecret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
			
			var user = new SyncSecretKey();
			user.setId(cryptoAsyncService.encryptWithPrivateKey(authentication.getName()));
			user.setSecretKey(encodedSecret);
			user.setExpirationTime(LocalDateTime.now().plusMinutes(ttl));
			user.setInstant(Instant.now());
			user.setExp(Instant.now().plus(ttl, ChronoUnit.MINUTES));
			user.setRsaPrivateKey (cryptoAsyncService.getPrivateKey());

			var hash = cryptoAsyncService.digest(user);
			var signature = cryptoAsyncService.sign(hash);
			
			user.setDigest(hash);
			user.setSignature(signature);
			
			secretKeys.put(authentication.getName(), user);
			
			var newUser = user.clone();
//			newUser.setSecretKey(cryptoAsyncService.encryptWithPrivateKey(encodedSecret));
			newUser.setSecretKey(new String(Base64.getEncoder().encodeToString(secretKey.getEncoded())));
			return newUser;
		}
	}

	private SecretKey generateAESKey(int keySize) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(alghoritm);
		keyGenerator.init(keySize);
		var key = keyGenerator.generateKey();
		return key;
	}

    public String decrypt(@NotBlank String id, String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, CertificateExpiredException {
		
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var name = authentication.getName();
		
		Assert.isTrue(name.equals(id),"id mistmacth");
		
		if (!secretKeys.containsKey(name)) 
			throw new InvalidKeyException("secret key not found for id "+name);
		if (secretKeys.get(name).getExpirationTime().isBefore(LocalDateTime.now())) {
			secretKeys.remove(name);
			throw new CertificateExpiredException("SecretKey has expired, please renew it");
		}
		
		var secretKey = secretKeys.get(name).getSecretKey().getBytes();  
		var key = new SecretKeySpec(secretKey, alghoritm);
		
		Cipher cipher = Cipher.getInstance(CryptoAesService.cipher);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);

		var plainText = cipher.doFinal(Base64.getDecoder().decode(data.getBytes()));
		var output = new String(plainText);
		
		return output;
	}
	
	public String encrypt(@NotEmpty String id, String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, CertificateExpiredException {
		
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var name = authentication.getName();
		
		Assert.isTrue(name.equals(id),"id mistmacth");
		
		if (!secretKeys.containsKey(name))
			throw new InvalidKeyException("secret key not found for id "+name);
		if (secretKeys.get(name).getExpirationTime().isBefore(LocalDateTime.now())) {
			secretKeys.remove(name);
			throw new CertificateExpiredException("SecretKey has expired, please renew it");
		}
		
		var secretKey = secretKeys.get(name).getSecretKey().getBytes(); 
		var key = new SecretKeySpec(secretKey, alghoritm);
		
		Cipher cipher = Cipher.getInstance(CryptoAesService.cipher);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		
		var cipherText = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
		var output = Base64.getEncoder().encodeToString(cipherText);
		return output;
	}
	
	private IvParameterSpec generateIv() {
	    byte[] iv = new byte[16];
	    new SecureRandom().nextBytes(iv);
	    return new IvParameterSpec(iv);
	}
}
