package com.up.fintech.armagedon.tp2.tp2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import lombok.extern.java.Log;

//@AutoConfigureMockMvc(addFilters = false, printOnlyOnFailure = false)
@Log
class WithdrawController_Test extends Abstract_BaseTest_Wallet {
	
	private BigDecimal amount =  BigDecimal.valueOf(20.0);
	private UUID wallet;
	
	@BeforeAll
	void setup() throws SQLException {
		log.info("Setup Withdraw");
		userUuid = UUID.randomUUID();
		email = "withdrawt@mail.com";//RandomConfirmationCode.generateRandomCode()+"@test.com";
		cuit = "withdraw"; //RandomConfirmationCode.generateRandomCode();
		log.info("Fin Setup Withdraw");
		Server.createWebServer("-web", "-webAllowOthers", "-webPort", "9092").start();
		
	}
	

	
	@Test
	@Order(1)
	void testWithdrawController_checkWithdrawIsNotEnabledforWalletWihtoutMoney() throws Exception {
		log.info("Verifico que solicitud de retiro no exista wallet ");
		checkSelfWallet_then200_thenupdateJsonWallet();
		wallet = UUID.fromString(variables.getWallet().get("walletId").asText());
		var link = variables.getWalletLinkRel("withdraw");
		assertNull(link);
		log.info("Fin Verifico que solicitud de retiro no exista wallet ");
	}
	
	@Test 
	@Order(2)
	void testDepositController_addMoneyToWallet_then201() throws Exception {
		depositAmountToWallet(amount);
		confirmDepositToWallet(amount);
	}
	
	@Test
	@Order(3)
	void testWithdrawController_withdrawAllRequestAndConfirmationToWalletAndCheckNewBalanceEqualsZero_then200() throws Exception {
		log.info("Envío y confirmo solicitud de retiro a wallet -> 201");
		checkSelfWallet_then200_thenupdateJsonWallet();
		var beforeBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
//		var beforeTotal = beforeBalance.multiply(BigDecimal.valueOf(0.98));
		withdrawRequest(beforeBalance, beforeBalance);
		withdrawConfirmation(beforeBalance, beforeBalance);
		checkSelfWallet_then200_thenupdateJsonWallet();
		var afterBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		assertEquals(BigDecimal.ZERO , afterBalance.stripTrailingZeros());
		log.info("Fin Envío y confirmo solicitud de retiro a wallet -> 200");
	}
	
	@Test
	@Order(4)
	void testWithdrawController_withdrawHalfRequestAndConfirmationToWalletAndCheckNewBalanceEqualsHalfMinusFee_then200() throws Exception {
		log.info("Envío y confirmo solicitud de retiro a wallet -> 201");
		testDepositController_addMoneyToWallet_then201();
		checkSelfWallet_then200_thenupdateJsonWallet();
		
		var beforeBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble()).divide(BigDecimal.valueOf(2));
		var total = beforeBalance.multiply(BigDecimal.valueOf(1.055));
		
		withdrawRequest(beforeBalance,total);
		withdrawConfirmation(beforeBalance,total);
		
		var fee = BigDecimal.valueOf(variables.getTransaction().get("fee").asDouble());
		
		checkSelfWallet_then200_thenupdateJsonWallet();
		var afterBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		
		assertEquals(beforeBalance.subtract(fee) , afterBalance);
		log.info("Fin Envío y confirmo solicitud de retiro a wallet -> 200");
	}
	@Test
	@Order(5)
	void testWithdrawController_checkFeeWalletBalance_then200() throws Exception {
		checkWallet_then200_thenupdateJsonWallet(new UUID(0, 0));
		
		var beforeBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		log.info("feeWallet balance before: "+beforeBalance.toPlainString());
		log.info("Envío y confirmo solicitud de retiro a wallet -> 201");
		checkWallet_then200_thenupdateJsonWallet(wallet);
		testDepositController_addMoneyToWallet_then201();
		testWithdrawController_withdrawAllRequestAndConfirmationToWalletAndCheckNewBalanceEqualsZero_then200();
		
		var fee = BigDecimal.valueOf(variables.getTransaction().get("fee").asDouble());
		log.info("Fee: : "+fee.toPlainString());
		
		checkWallet_then200_thenupdateJsonWallet(new UUID(0, 0));
		var afterBalance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		log.info("feeWallet balance after: "+afterBalance.toPlainString());
		assertEquals(beforeBalance.add(fee), afterBalance);
		log.info("Fin Envío y confirmo solicitud de retiro a wallet -> 200");
		
	}
	
	@Test 
	@Order(6)
	void testWithdratController_getQrFo_then200() throws Exception {
		log.info("***Test pido QR deposito en wallet -> 200");
		checkWallet_then200_thenupdateJsonWallet(wallet);
		depositAmountToWallet(amount);
		confirmDepositToWallet(amount);
		checkWallet_then200_thenupdateJsonWallet(wallet);
		var balance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		withdrawRequest(balance, balance);
		var link = variables.getTransactionLinkRel("qr");
		mvc.perform(get(link))
				.andExpect(status().isOk());
		log.info("***Fin Test pido QR deposito en wallet -> 200");
	}
	
	@Test 
	@Order(7)
	void testWithdratController_withdraLimit_then400() throws Exception {
		log.info("***Test pido QR deposito en wallet -> 200");
		checkWallet_then200_thenupdateJsonWallet(wallet);
		depositAmountToWallet(amount);
		confirmDepositToWallet(amount);
		checkWallet_then200_thenupdateJsonWallet(wallet);
		var balance= BigDecimal.valueOf(variables.getWallet().get("balance").asDouble());
		withdrawRequest400(balance.multiply(BigDecimal.valueOf(0.99)));
		
		log.info("***Fin Test pido QR deposito en wallet -> 200");
	}
}
