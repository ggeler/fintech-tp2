package com.up.fintech.armagedon.tp2.tp2.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.springframework.stereotype.Service;
import com.up.fintech.armagedon.tp2.tp2.entity.RsaPublicKey;

@Service
public class CryptoAsyncService {

	private static final String signature = "SHA256withRSA";
	private static final String alghoritm = "RSA";
	private static final String messageDigest = "SHA-256";
	
	private final KeyPair keyPair;
	private final String rsaPublicKeyBase64;
	private final String privateKey;
	
	public CryptoAsyncService() throws NoSuchAlgorithmException {
		keyPair = generateRsaKeyPair();
		var publicKey = keyPair.getPublic().getEncoded();
		var privateKey = keyPair.getPrivate().getEncoded();
		this.rsaPublicKeyBase64 = new String(Base64.getEncoder().encode(publicKey));
		this.privateKey = new String(Base64.getEncoder().encode(privateKey));
	}
	
	public RsaPublicKey getPublicKey() {
		return new RsaPublicKey(rsaPublicKeyBase64);
	}
	
	private KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(alghoritm);
		keyGen.initialize(1024);
		var keyPair = keyGen.generateKeyPair();
		return keyPair;
	}
	
	private PrivateKey getRsaPrivateKey(String rsaPrivateKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var privateKey = Base64.getDecoder().decode(rsaPrivateKeyBase64);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory keyFactory = KeyFactory.getInstance(alghoritm);
		var key = keyFactory.generatePrivate(keySpec);
		return key;
	}
	
	private PublicKey getRsaPublicKey(String rsaPublicKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var publicKey = Base64.getDecoder().decode(rsaPublicKeyBase64);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance(alghoritm);
		var key = keyFactory.generatePublic(spec);
		return key;
	}

	public boolean verifySignature(String signature, String data, String rsaPublicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
		PublicKey publicKey = getRsaPublicKey(rsaPublicKey);
		Signature sign = Signature.getInstance(CryptoAsyncService.signature);
		sign.initVerify(publicKey);
		sign.update(data.getBytes()); //StandardCharsets.UTF_8
		var verify = sign.verify(Base64.getDecoder().decode(signature));
		return verify;
	}
	
	String sign(String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
		var rsaPrivateKey = this.privateKey;
		PrivateKey privateKey = getRsaPrivateKey(rsaPrivateKey);
		Signature sign = Signature.getInstance(signature);
		sign.initSign(privateKey);
		sign.update(Base64.getDecoder().decode(data.getBytes()));
		var signature = Base64.getEncoder().encodeToString(sign.sign());
		return signature;
	}
	
//	String sign(Object data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {
//		var rsaPrivateKey = this.privateKey;
//		PrivateKey privateKey = getRsaPrivateKey(rsaPrivateKey);
//		Signature sign = Signature.getInstance(signature);
//		
//		sign.initSign(privateKey);
//		sign.update(Base64.getDecoder().decode(digest(data)));
//		
//		var signature = Base64.getEncoder().encodeToString(sign.sign());
//		return signature;
//	}
	
	String digest(Object data) throws NoSuchAlgorithmException, IOException {
		var md = MessageDigest.getInstance(messageDigest);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(out);
		stream.writeObject(data);
		stream.flush();
		var hash = md.digest(out.toByteArray());
		
		return Base64.getEncoder().encodeToString(hash);
	}
	private String decryptWithPrivaterKey(String data, String base64PrivateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(alghoritm);///ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, getRsaPrivateKey(base64PrivateKey));
		var encodedData = Base64.getDecoder().decode(data);
		var decryptedData = cipher.doFinal(encodedData);
		return new String(decryptedData);
	}
	
	private String decryptWithPublicKey(String data, String base64PublicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(alghoritm);///ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, getRsaPublicKey(base64PublicKey));
		var encodedData = Base64.getDecoder().decode(data);
		var decryptedData = cipher.doFinal(encodedData);
		return new String(decryptedData);
	}

	String encryptWithPrivateKey(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		var base64PrivateKey = this.privateKey;
		Cipher cipher = Cipher.getInstance(alghoritm); ///ECB/PKCS1Padding
		cipher.init(Cipher.ENCRYPT_MODE, getRsaPrivateKey(base64PrivateKey));
		var encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
		var encodedData = Base64.getEncoder().encodeToString(encryptedData); 
		return encodedData;
	}
	
	private String encryptWithPublicKey(String data, String base64PublicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(alghoritm); ///ECB/PKCS1Padding
		cipher.init(Cipher.ENCRYPT_MODE, getRsaPublicKey(base64PublicKey));
		var encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
		var encodedData = Base64.getEncoder().encodeToString(encryptedData); 
		return encodedData;
	}
}
