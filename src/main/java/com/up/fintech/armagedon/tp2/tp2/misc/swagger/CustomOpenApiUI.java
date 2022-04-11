package com.up.fintech.armagedon.tp2.tp2.misc.swagger;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@SecurityScheme(name = "OAuth2", type = SecuritySchemeType.OAUTH2, scheme = "bearer",bearerFormat = "jwt", in = SecuritySchemeIn.HEADER, flows = @OAuthFlows(
		authorizationCode = @OAuthFlow(
				authorizationUrl = "https://oauth2-fintech-palermo.herokuapp.com/auth/realms/fintech/protocol/openid-connect/auth",
				tokenUrl = "https://oauth2-fintech-palermo.herokuapp.com/auth/realms/fintech/protocol/openid-connect/token",
				refreshUrl = "https://oauth2-fintech-palermo.herokuapp.com/auth/realms/fintech/protocol/openid-connect/token",
				scopes = {@OAuthScope(name = "email")})
//		,password = @OAuthFlow(
//				tokenUrl = "https://oauth2-fintech-palermo.herokuapp.com/auth/realms/fintech/protocol/openid-connect/token",
//				refreshUrl = "https://oauth2-fintech-palermo.herokuapp.com/auth/realms/fintech/protocol/openid-connect/token",
//				scopes = {@OAuthScope(name = "email")})
		)
	)
@OpenAPIDefinition(security = @SecurityRequirement(name = "OAuth2"),
	info = @Info(contact = @Contact(email = "ggeler@hotmail.com",name = "Gastón Geler"),description = "TP2 Api Bankend Documentation",title = "TP2 Fintech API Backend",
	version = "0.3", license = @License(name = "Apache 2.0",url = "https://www.apache.org/licenses/LICENSE-2.0"))
)
public class CustomOpenApiUI {
//
//	 @Bean
//	 public OpenAPI springShopOpenAPI() {
//		 return new OpenAPI()
//				 .info(new Info().title("TP2 Fintech API Backend")
//						 .description("TP2 Api Bankend Documentation")
//						 .version("v0.0.1")
//						 .license(new License().name("Licence Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
//				 .externalDocs(new ExternalDocumentation()
//						 .description("ISO 8583 Documentation")
//						 .url("https://en.wikipedia.org/wiki/ISO_8583")
//						 .description("Json Documentation")
//						 .url("https://en.wikipedia.org/wiki/JSON"));
//	 }
}
