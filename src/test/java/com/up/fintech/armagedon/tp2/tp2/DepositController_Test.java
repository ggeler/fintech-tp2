package com.up.fintech.armagedon.tp2.tp2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import lombok.extern.java.Log;

@Log
class DepositController_Test extends Abstract_BaseTest_Wallet {
	
	private BigDecimal amount = BigDecimal.valueOf(100);
	
	@BeforeAll
	void setup() {
		log.info("Setup Deposit");
		userUuid = UUID.randomUUID();
		email = "testdeposit@mail.com";//RandomConfirmationCode.generateRandomCode()+"@test.com";
		cuit = "deposit"; //RandomConfirmationCode.generateRandomCode();
		log.info("Fin Setup Deposit");
	}
	
	@Test 
	@Order(1)
	void testDepositController_submitDepositToWallet_then201() throws Exception {
//		var walletId = variables.getWalletId();
		log.info("***Test envió deposito a wallet -> 201");
		checkSelfWallet_then200_thenupdateJsonWallet();
		var beforeBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		depositAmountToWallet(amount);
		confirmDepositToWallet(amount);
		checkSelfWallet_then200_thenupdateJsonWallet();
		var afterBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		assertEquals(beforeBalance.add(amount), afterBalance);
		log.info("***Fin envió deposito a wallet -> 201");
	}
	
	@Test 
	@Order(3)
	void testDepositController_cancelDepositToWallet_then200() throws Exception {
		log.info("***Test envío y cancelo deposito en wallet -> 200");
		checkSelfWallet_then200_thenupdateJsonWallet();
		var beforeBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		depositAmountToWallet(amount);
		cancelDepositToWallet();
		checkSelfWallet_then200_thenupdateJsonWallet();
		var afterBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		assertEquals(beforeBalance, afterBalance);
		log.info("***fin envío y cancelo deposito en wallet -> 200");
	}
	
	@Test 
	@Order(4)
	void testDepositController_confirmACanceledDepositToWallet_then404() throws Exception {
		log.info("***Test confirmo un deposito cancelado en wallet -> 400");
		checkSelfWallet_then200_thenupdateJsonWallet();
		var beforeBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		depositAmountToWallet(amount);
		var link = variables.getTransaction();
		confirmDepositToWallet(amount);
		variables.setTransaction(link);
		cancelDepositToWallet_then400(variables.getTransactionLinkRel("cancel"));
		checkSelfWallet_then200_thenupdateJsonWallet();
		var newBalance = BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		assertEquals(beforeBalance.add(amount), newBalance);
		log.info("***Fin Test confirmo un deposito cancelado en wallet -> 400");
	}
	@Test 
	@Order(5)
	void testDepositController_getQrForDepositToWallet_then200() throws Exception {
		log.info("***Test pido QR deposito en wallet -> 200");
		depositAmountToWallet(amount);
		var link = variables.getTransactionLinkRel("qr");
		mvc.perform(get(link))
				.andExpect(status().isOk());
		log.info("***Fin Test pido QR deposito en wallet -> 200");
	}
	@Test 
	@Order(6)
	void testDepositController_submitNegativeDepositToWallet_then400() throws Exception {
		log.info("***Test envió deposito con monto negativo a wallet -> 400");
		checkSelfWallet_then200_thenupdateJsonWallet();
		var beforeBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		depositNegativeAmountToWallet(amount);
		
		checkSelfWallet_then200_thenupdateJsonWallet();
		var newBalance = BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		assertEquals(beforeBalance, newBalance);
		log.info("***Fin envió deposito negativo a wallet -> 400");
	}
}
