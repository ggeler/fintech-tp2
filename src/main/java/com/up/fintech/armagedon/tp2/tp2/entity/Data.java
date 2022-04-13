package com.up.fintech.armagedon.tp2.tp2.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@lombok.Data
public class Data {

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String internalData;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String interfaceFrom;
}
