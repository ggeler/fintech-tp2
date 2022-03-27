package com.up.fintech.armagedon.tp2.tp2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@lombok.Data
public class Data {

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	String internalData;
	String interfaceFrom;
}
