package com.up.fintech.armagedon.misc.config;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.up.fintech.armagedon.tp3.service.CryptoAesService;
import com.up.fintech.armagedon.tp3.service.CryptoRsaService;

@Configuration
public class ServerConfig {

	@Bean
	public CryptoRsaService getCryptoAsyncServceInstance() throws NoSuchAlgorithmException {
		return CryptoRsaService.getInstance();
	}
	
	@Bean
	public CryptoAesService getCryptoSyncServceInstance(@Autowired CryptoRsaService asyncService) throws NoSuchAlgorithmException {
		return CryptoAesService.getInstance(asyncService);
	}
}
