package com.up.fintech.armagedon.tp2.tp2;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import lombok.extern.java.Log;

@Log
public class WalletController_Test extends Abstract_BaseTest_Wallet {
	
	@BeforeAll
	void setup() {
		log.info("Setup Wallet ");
		userUuid = UUID.randomUUID();
		email = "wallet@mail.com";//RandomConfirmationCode.generateRandomCode()+"@test.com";
		cuit = "wallet"; //RandomConfirmationCode.generateRandomCode();
		log.info("Fin Setup Wallet");
	}
	@Test 
	@Order(1)
	void testWalletController_checkExistingWallet_then200() throws Exception {
//		var tmp = variables.getWalletId();
		var link = variables.getLinkRel(variables.getJsonWallet().get("links"), "self");
		mvc.perform(get(link))
			.andExpect(status().isOk());
	}

	@Test 
	@Order(2)
	void testWalletController_getwWalletByUser_then200() throws Exception {
		mvc.perform(get("/fintech/wallet")
				.queryParam("user", userUuid.toString()))
				.andExpect(status().isOk());
	}
	
	@Test 
	@Order(3)
	void testWalletController_getNonExistentWalletByWalletUUID_then404() throws Exception {
		mvc.perform(get("/fintech/wallet/{wallet}", UUID.randomUUID().toString()))
				.andExpect(status().isNotFound());
	}
	
	@Test 
	@Order(4)
	void testWalletController_createNewWalletForAnExistingEmail_then409() throws Exception {
		mvc.perform(post("/fintech/wallet")
				.queryParam("user", userUuid.toString()).queryParam("email", email).queryParam("cuit", "123"))
				.andExpect(status().isConflict())
//				.andExpect(jsonPath("$.data.walletId").exists())
//				.andExpect(jsonPath("$.data.walletId").)
				.andReturn();
	}
		
	@Test 
	@Order(4)
	void testWalletController_createNewWalletDuplicatingCuit_then409() throws Exception {
		mvc.perform(post("/fintech/wallet")
				.queryParam("user", userUuid.toString()).queryParam("email", "random@mail").queryParam("cuit", cuit))
				.andExpect(status().isConflict())
//				.andExpect(jsonPath("$.data.walletId").exists())
//				.andExpect(jsonPath("$.data.walletId").)
				.andReturn();
	}
}
