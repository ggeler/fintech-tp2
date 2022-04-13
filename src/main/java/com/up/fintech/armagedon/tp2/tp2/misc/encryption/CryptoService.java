package com.up.fintech.armagedon.tp2.tp2.misc.encryption;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp2.tp2.entity.User;

@Service
public class CryptoService {

	private static final String signature = "SHA256withRSA";
	private static final String alghoritm = "RSA";
	private static final String cipher = "AES";
	
	public User assignRsaKeyPair() throws NoSuchAlgorithmException   {
		
		var keyPair = generateRsaKeyPair();
		var publicKey = keyPair.getPublic().getEncoded();
		var privateKey = keyPair.getPrivate().getEncoded();
		var rsaPublicKeyBase64 = new String(Base64.getEncoder().encode(publicKey));
		var rsaPrivateKeyBase64 = new String(Base64.getEncoder().encode(privateKey));
		
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		
		var user = new User();
		user.setId(authentication.getName());
		user.setRsaPrivateKey(rsaPrivateKeyBase64);
		user.setRsaPublicKey(rsaPublicKeyBase64);
		return user;
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
		Signature sign = Signature.getInstance(CryptoService.signature);
		sign.initVerify(publicKey);
		sign.update(data.getBytes()); //StandardCharsets.UTF_8
		var verify = sign.verify(Base64.getDecoder().decode(signature));
		return verify;
	}
	
	public String sign(String data, String rsaPrivateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
		PrivateKey privateKey = getRsaPrivateKey(rsaPrivateKey);
		Signature sign = Signature.getInstance(signature);
		sign.initSign(privateKey);
		sign.update(data.getBytes());
		var signature = Base64.getEncoder().encodeToString(sign.sign());
		return signature;
	}
	
	public String decryptWithPrivaterKey(String data, String base64PrivateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(alghoritm);///ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, getRsaPrivateKey(base64PrivateKey));
		var encodedData = Base64.getDecoder().decode(data);
		var decryptedData = cipher.doFinal(encodedData);
		return new String(decryptedData);
	}
	
	public String decryptWithPublicKey(String data, String base64PublicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(alghoritm);///ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, getRsaPublicKey(base64PublicKey));
		var encodedData = Base64.getDecoder().decode(data);
		var decryptedData = cipher.doFinal(encodedData);
		return new String(decryptedData);
	}

	public String encryptWithPrivateKey(String data, String base64PrivateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(alghoritm); ///ECB/PKCS1Padding
		cipher.init(Cipher.ENCRYPT_MODE, getRsaPrivateKey(base64PrivateKey));
		var encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
		var encodedData = Base64.getEncoder().encodeToString(encryptedData); 
		return encodedData;
	}
	
	public String encryptWithPublicKey(String data, String base64PublicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(alghoritm); ///ECB/PKCS1Padding
		cipher.init(Cipher.ENCRYPT_MODE, getRsaPublicKey(base64PublicKey));
		var encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
		var encodedData = Base64.getEncoder().encodeToString(encryptedData); 
		return encodedData;
	}
	
	public SecretKey generateAESKey(int keySize) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(cipher);
		keyGenerator.init(keySize);
		var key = keyGenerator.generateKey();
		return key;
	}
	
	
//	public byte[] decryptWithAes(byte[] data, byte[] aesKey, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//		Cipher cipher = Cipher.getInstance("RSA/CBC/PKCSSPadding");
//		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(aesKey, "AES"), new IvParameterSpec(iv));
//		var decryptedData = cipher.doFinal(data);
//		return decryptedData;
//	}
}
