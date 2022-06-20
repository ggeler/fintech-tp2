package com.up.fintech.armagedon.tp4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventTransferDto {

	private long eventKey;
	private int homeTeamResult;
	private int awayTeamResult;
}
