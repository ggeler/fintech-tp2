package com.up.fintech.armagedon.tp2.tp2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@Log
class DepositController_Test extends Abstract_BaseTest_Wallet {
	
	private double amount = 100;
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
		var link = variables.getLinkRel(variables.getJsonWallet().get("links"), "deposit");
		var result = mvc.perform(post(link ) 
				.content("{\"amount\":"+amount+"}").contentType(MediaType.APPLICATION_JSON).characterEncoding(Charset.defaultCharset()))
				.andExpect(status().isCreated())
				.andReturn();
		var json = new ObjectMapper().readTree(result.getResponse().getContentAsString());
		links = json.get("data").get("links");
		log.info("***Fin envió deposito a wallet -> 201");
	}
	
	@Test 
	@Order(2)
	void testDepositController_confirmDepositToWalletAndNewBalance_then200() throws Exception {
		log.info("***Test confirmo deposito en wallet -> 200");
		var link = variables.getLinkRel(links, "confirm");
		var beforeBalance= variables.getJsonWallet().get("balance").asDouble();
		mvc.perform(put(link))
				.andExpect(status().isOk())
				.andExpect(jsonPath("data.amount", is(amount)))
				.andReturn();
		baseTest_walletController_checkExistingWallet_then200_thenupdateJsonWallet();
		var newBalance = variables.getJsonWallet().get("balance").asDouble();
		assertEquals(beforeBalance+amount, newBalance);
		log.info("***Fin confirmo deposito en wallet -> 200");
	}
	@Test 
	@Order(3)
	void testDepositController_cancelDepositToWallet_then200() throws Exception {
		log.info("***Test envío y cancelo deposito en wallet -> 200");
		testDepositController_submitDepositToWallet_then201();
		var link = variables.getLinkRel(links, "cancel");
		mvc.perform(delete(link))
				.andExpect(status().isOk())
				.andReturn();
		log.info("***fin envío y cancelo deposito en wallet -> 200");
	}
	
	@Test 
	@Order(4)
	void testDepositController_confirmACanceledDepositToWallet_then404() throws Exception {
		log.info("***Test confirmo un deposito cancelado en wallet -> 400");
		var link = variables.getLinkRel(links, "confirm");
		mvc.perform(put(link))
				.andExpect(status().isBadRequest())
				.andReturn();
		log.info("***Fin Test confirmo un deposito cancelado en wallet -> 400");
	}
	@Test 
	@Order(5)
	void testDepositController_getQrForDepositToWallet_then200() throws Exception {
		log.info("***Test pido QR deposito en wallet -> 200");
		testDepositController_submitDepositToWallet_then201();
		var link = variables.getLinkRel(links, "qr");
		mvc.perform(get(link))
				.andExpect(status().isOk());
		log.info("***Fin Test pido QR deposito en wallet -> 200");
	}
	@Test 
	@Order(6)
	void testDepositController_submitNegativeDepositToWallet_then400() throws Exception {
		log.info("***Test envió deposito con monto negativo a wallet -> 400");
		var beforeBalance= variables.getJsonWallet().get("balance").asDouble();
		var link = variables.getLinkRel(variables.getJsonWallet().get("links"), "deposit");
		
		mvc.perform(post(link ) 
				.content("{\"amount\":"+(amount*-1)+"}").contentType(MediaType.APPLICATION_JSON).characterEncoding(Charset.defaultCharset()))
				.andExpect(status().isBadRequest());
		baseTest_walletController_checkExistingWallet_then200_thenupdateJsonWallet();
		var newBalance = variables.getJsonWallet().get("balance").asDouble();
		assertEquals(beforeBalance, newBalance);
		log.info("***Fin envió deposito negativo a wallet -> 400");
	}
}
