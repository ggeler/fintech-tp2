package com.up.fintech.armagedon.tp4.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.up.fintech.armagedon.tp4.controller.assembler.TransactionAssembler;
import com.up.fintech.armagedon.tp4.dto.EventTransferDto;
import com.up.fintech.armagedon.tp4.dto.ExternalTransferDto;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.bet.Event;
import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.entity.state.transaction.TransactionStatusEnum;
import com.up.fintech.armagedon.tp4.service.BetService;
import com.up.fintech.armagedon.tp4.service.EventService;
import com.up.fintech.armagedon.tp4.service.TransactionService;
import com.up.fintech.armagedon.tp4.service.WalletService;

@RestController
@RequestMapping("/fintech/event")
public class EventController {

	private final EventService eventService;
	
	public EventController(EventService eventService) {

		this.eventService = eventService;
	}
	
	@PutMapping("")
	public ResponseEntity<ResponseStatusWrapper<EntityModel<Transaction>>> request(@RequestBody EventTransferDto event) {
		eventService.update(event);
//		var model = assembler.toModel(request);
//		var response = new ResponseStatusWrapper<>(model,true,0,"Previsualización de Apuesta");
		return ResponseEntity.ok(null);
	}
	
}