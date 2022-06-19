package com.up.fintech.armagedon.tp2.tp2;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.java.Log;


//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class, HibernateJpaAutoConfiguration.class, 
//		OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class})
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false, printOnlyOnFailure = true)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@Log
abstract class Abstract_BaseTest_Wallet {

	UUID userUuid;//new UUID(1,1);
	@Autowired MockMvc mvc;
	TestVariables variables = new TestVariables();
	
	String email;
	String cuit;
	
	@Test
	@Order(0)
	void createSelfNewWallet_then201() throws Exception {
		log.info("Base test - crear wallet");
		var result = mvc.perform(post("/fintech/wallet")
				.queryParam("user", userUuid.toString()).queryParam("email", email).queryParam("cuit", cuit))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.walletId").exists())
				.andReturn();
		variables.setWallet(result);
		log.info("Fin Base test - crear wallet "+userUuid.toString()+" "+email+" "+cuit);
	}
	
	void checkSelfWallet_then200_thenupdateJsonWallet() throws Exception {
		log.info("Leo wallet para obenetr links wallet -> 201");
		var link = variables.getWalletLinkRel("self");
		var result = mvc.perform(get(link))
			.andExpect(status().isOk())
			.andReturn();
		variables.setWallet(result);
	}
	
	void checkWallet_then200_thenupdateJsonWallet(UUID wallet) throws Exception {
		log.info("Leo wallet para obenetr links wallet -> 201");
		var result = mvc.perform(get("/fintech/wallet/{wallet}",wallet))
			.andExpect(status().isOk())
			.andReturn();
		variables.setWallet(result);
	}
	void depositAmountToWallet(BigDecimal amount) throws Exception {
		log.info("Envío solicitud de deposito a wallet -> 201");
		var link = variables.getWalletLinkRel("deposit");
		var result = mvc.perform(post(link)
				.content("{\"amount\":"+amount.doubleValue()+"}").contentType(MediaType.APPLICATION_JSON).characterEncoding(Charset.defaultCharset()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("data.amount", is(amount.doubleValue())))
				.andReturn();
		variables.setTransaction(result);
		log.info("Fin  solicitud de deposito a wallet -> 201");
	}
	void depositNegativeAmountToWallet(BigDecimal amount) throws Exception {
		log.info("Envío solicitud de deposito a wallet -> 201");
		var link = variables.getWalletLinkRel("deposit");
		var result = mvc.perform(post(link)
				.content("{\"amount\":"+amount.negate().doubleValue()+"}").contentType(MediaType.APPLICATION_JSON).characterEncoding(Charset.defaultCharset()))
				.andExpect(status().isBadRequest())
				.andReturn();
		variables.setTransaction(result);
		log.info("Fin  solicitud de deposito a wallet -> 201");
	}
	void confirmDepositToWallet(BigDecimal amount) throws Exception {
		log.info("inicio confirmación de deposito a wallet -> 200");
		var link = variables.getTransactionLinkRel("confirm");
		var result = mvc.perform(put(link))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.amount", is(amount.doubleValue())))
			.andReturn();
		variables.setTransaction(result);
		log.info("fin confirmación de deposito a wallet -> 200");
	}
	void cancelDepositToWallet() throws Exception {
		log.info("inicio cancelacion de deposito a wallet -> 200");
		var link = variables.getTransactionLinkRel("cancel");
		var result = mvc.perform(delete(link))
			.andExpect(status().isOk())
			.andReturn();
		variables.setTransaction(result);
		log.info("fin cancelación de deposito a wallet -> 200");
	}
	
	void cancelDepositToWallet_then400(String link) throws Exception {
		log.info("inicio cancelacion de deposito a wallet -> 200");
		
		var result = mvc.perform(delete(link))
			.andExpect(status().isBadRequest())
			.andReturn();
		variables.setTransaction(result);
		log.info("fin cancelación de deposito a wallet -> 200");
	}
	void withdrawRequest(BigDecimal amount, BigDecimal fee) throws Exception {
		log.info("inicio confirmación de retiro a wallet -> 200");
		var link = variables.getWalletLinkRel("withdraw");
		var result = mvc.perform(post(link) 
				.content("{\"amount\":"+amount.doubleValue()+"}").contentType(MediaType.APPLICATION_JSON).characterEncoding(Charset.defaultCharset()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("data.total", is(fee.doubleValue())))
				.andReturn();
		variables.setTransaction(result);
		log.info("Fin confirmación de retiro a wallet -> 200");
	}
	
	void withdrawConfirmation(BigDecimal amount, BigDecimal fee) throws Exception {
		var link = variables.getTransactionLinkRel("confirm");
		var result = mvc.perform(put(link))
				.andExpect(status().isOk())
				.andExpect(jsonPath("data.total", is(fee.doubleValue())))
				.andReturn();
		variables.setTransaction(result);
	}
}
