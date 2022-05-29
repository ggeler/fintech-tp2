package com.up.fintech.armagedon.misc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@EnableGlobalAuthentication
//@EnableMethodSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {

//	@Override
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().antMatchers("/actuator/**","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/favicon.ico","/error").permitAll()
			.and().authorizeRequests().antMatchers(HttpMethod.POST, "/fintech/external/bank/transfer/**").permitAll()
			.and().csrf().ignoringAntMatchers("/fintech/external/bank/transfer/**")
		.and()
			.requestMatchers().antMatchers("/**")
		.and()
			.authorizeRequests().anyRequest().authenticated()
		.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
//			.anonymous().disable()
			.formLogin().disable()
			.httpBasic().disable()
			.oauth2ResourceServer().jwt();
		return http.build();
	}
	
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/actuator/**","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/favicon.ico"); //,"/index/**","/**");
//		super.configure(web);
//	}
}
