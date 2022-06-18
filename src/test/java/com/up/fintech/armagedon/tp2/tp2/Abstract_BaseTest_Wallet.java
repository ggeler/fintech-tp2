package com.up.fintech.armagedon.tp2.tp2;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@AutoConfigureMockMvc(addFilters = false, printOnlyOnFailure = true)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class, HibernateJpaAutoConfiguration.class, 
//		OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class})
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@Log
abstract class Abstract_BaseTest_Wallet {

	UUID userUuid;//new UUID(1,1);
	@Autowired MockMvc mvc;
	TestVariables variables = new TestVariables();
	
	String email;
	String cuit;
	
	JsonNode links;
	
	@Test
	@Order(0)
	void baseTest_walletController_createNewWallet_then201() throws Exception {
		log.info("Base test - crear wallet");
		var result = mvc.perform(post("/fintech/wallet")
				.queryParam("user", userUuid.toString()).queryParam("email", email).queryParam("cuit", cuit))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.walletId").exists())
//				.andExpect(jsonPath("$.data.walletId").)
				.andReturn();
		
		var json = new ObjectMapper().readTree(result.getResponse().getContentAsString());
		var jsonNode = json.get("data");
		variables.setJsonWallet(jsonNode);
//		variables.setLink(jsonNode.get("links"));
		log.info("Fin Base test - crear wallet "+userUuid.toString()+" "+email+" "+cuit);
	}
	
	void baseTest_walletController_checkExistingWallet_then200_thenupdateJsonWallet() throws Exception {
		log.info("Leo wallet para obenetr links wallet -> 201");
		var link = variables.getLinkRel(variables.getJsonWallet().get("links"), "self");
		var result = mvc.perform(get(link))
			.andExpect(status().isOk())
			.andReturn();
		var json = new ObjectMapper().readTree(result.getResponse().getContentAsString());
		variables.setJsonWallet(json.get("data"));
	}
}
