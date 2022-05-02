package com.up.fintech.armagedon.misc.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().antMatchers("/actuator/**","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/favicon.ico").permitAll()
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
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/actuator/**","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/favicon.ico"); //,"/index/**","/**");
		super.configure(web);
	}
}
