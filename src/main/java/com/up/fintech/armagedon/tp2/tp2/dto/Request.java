package com.up.fintech.armagedon.tp2.tp2.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class Request {

	@JsonAlias("interface")
	private String interfaceOrigin;
	
}
