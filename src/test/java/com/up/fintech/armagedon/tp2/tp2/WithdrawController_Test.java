package com.up.fintech.armagedon.tp2.tp2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

//@AutoConfigureMockMvc(addFilters = false, printOnlyOnFailure = false)
@Log
class WithdrawController_Test extends Abstract_BaseTest_Wallet {
	
	private BigDecimal amount =  BigDecimal.valueOf(20.0);
	
	@BeforeAll
	void setup() {
		log.info("Setup Withdraw");
		userUuid = UUID.randomUUID();
		email = "withdrawt@mail.com";//RandomConfirmationCode.generateRandomCode()+"@test.com";
		cuit = "withdraw"; //RandomConfirmationCode.generateRandomCode();
		log.info("Fin Setup Withdraw");
	}
	
	@Test
	@Order(1)
	void testWithdrawController_checkWithdrawIsNotEnabledforWalletWihtoutMoney() throws Exception {
		log.info("Verifico que solicitud de retiro no exista wallet ");
		baseTest_walletController_checkExistingWallet_then200_thenupdateJsonWallet();
		var link = variables.getLinkRel(variables.getJsonWallet().get("links"), "withdraw");
		assertNull(link);
		log.info("Fin Verifico que solicitud de retiro no exista wallet ");
	}
	
	@Test 
	@Order(2)
	void testDepositController_addMoneyToWallet_then201() throws Exception {
		log.info("Envío y confirmo solicitud de deposito a wallet -> 201");
		var link = variables.getLinkRel(variables.getJsonWallet().get("links"), "deposit");
		var result = mvc.perform(post(link)
				.content("{\"amount\":"+amount+"}").contentType(MediaType.APPLICATION_JSON).characterEncoding(Charset.defaultCharset()))
				.andExpect(status().isCreated())
				.andReturn();
		
		var json = new ObjectMapper().readTree(result.getResponse().getContentAsString());
		var jsonNode = json.get("data").get("links");
		
		link = variables.getLinkRel(jsonNode, "confirm");
		result = mvc.perform(put(link))
			.andExpect(status().isOk())
			.andReturn();
		json = new ObjectMapper().readTree(result.getResponse().getContentAsString());
		
		log.info("fin Envío solicitud y confirmación de deposito a wallet -> 200");
	}
	
	@Test
	@Order(3)
	void testWithdrawController_withdrawAllRequestAndConfirmationToWalletAndCheckNewBalanceEqualsZero_then200() throws Exception {
		log.info("Envío y confirmo solicitud de retiro a wallet -> 201");
		baseTest_walletController_checkExistingWallet_then200_thenupdateJsonWallet();
		var beforeBalance= BigDecimal.valueOf(variables.getJsonWallet().get("balance").asDouble());
		var link = variables.getLinkRel(variables.getJsonWallet().get("links"), "withdraw");
		var result = mvc.perform(post(link) 
				.content("{\"amount\":"+amount+"}").contentType(MediaType.APPLICATION_JSON).characterEncoding(Charset.defaultCharset()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("data.total", is(amount.doubleValue())))
				.andReturn();
		var json = new ObjectMapper().readTree(result.getResponse().getContentAsString());
		var jsonNode = json.get("data").get("links");
		link = variables.getLinkRel(jsonNode, "confirm");
		result = mvc.perform(put(link))
				.andExpect(status().isOk())
				.andReturn();
		baseTest_walletController_checkExistingWallet_then200_thenupdateJsonWallet();
		var afterBalance= BigDecimal.valueOf(variables.getJsonWallet().get("balance").asDouble());
		assertEquals(beforeBalance.subtract(amount) , afterBalance);
		log.info("Fin Envío y confirmo solicitud de retiro a wallet -> 200");
		
	}
	
	@Test
	@Order(4)
	void testWithdrawController_withdrawHalfRequestAndConfirmationToWalletAndCheckNewBalanceEqualsHalfMinusFee_then200() throws Exception {
		log.info("Envío y confirmo solicitud de retiro a wallet -> 201");
		testDepositController_addMoneyToWallet_then201();
		baseTest_walletController_checkExistingWallet_then200_thenupdateJsonWallet();
		
		var beforeBalance= BigDecimal.valueOf(variables.getJsonWallet().get("balance").asDouble());
		var link = variables.getLinkRel(variables.getJsonWallet().get("links"), "withdraw");
		var result = mvc.perform(post(link) 
				.content("{\"amount\":"+amount.divide(BigDecimal.valueOf(2))+"}").contentType(MediaType.APPLICATION_JSON).characterEncoding(Charset.defaultCharset()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("data.total", is(amount.divide(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(1.02)).doubleValue())))
				.andReturn();
		var json = new ObjectMapper().readTree(result.getResponse().getContentAsString());
		var jsonNode = json.get("data").get("links");
		link = variables.getLinkRel(jsonNode, "confirm");
		result = mvc.perform(put(link))
				.andExpect(status().isOk())
				.andReturn();
		baseTest_walletController_checkExistingWallet_then200_thenupdateJsonWallet();
		var afterBalance= BigDecimal.valueOf(variables.getJsonWallet().get("balance").asDouble());
		var newBalance = beforeBalance.subtract(amount.divide(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(1.02))).stripTrailingZeros();
		assertEquals(newBalance, afterBalance);
		log.info("Fin Envío y confirmo solicitud de retiro a wallet -> 200");
		
	}
	
}
