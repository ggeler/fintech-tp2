package com.up.fintech.armagedon.tp2.tp2.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class CustomOpenApiUI {

	 @Bean
	 public OpenAPI springShopOpenAPI() {
		 return new OpenAPI()
				 .info(new Info().title("TP2 Fintech API Backend")
						 .description("TP2 Api Bankend Documentation")
						 .version("v0.0.1")
						 .license(new License().name("Licence Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
				 .externalDocs(new ExternalDocumentation()
						 .description("ISO 8583 Documentation")
						 .url("https://en.wikipedia.org/wiki/ISO_8583"));
	 }
}
