package com.up.fintech.armagedon.tp4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonView;
import com.up.fintech.armagedon.tp4.entity.ExternalReceiveTransfer;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.misc.component.Views;
import com.up.fintech.armagedon.tp4.service.TransferService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/fintech/external/bank/transfer")
//@PreAuthorize("permitAll()")
@Log4j2
public class ExternalTransferController {

	private final TransferService service;
	
	@Autowired
	public ExternalTransferController(TransferService service) {
		this.service = service;
	}
	
	@PostMapping
//	@ApiResponses( value = {
//			@ApiResponse(responseCode = "200", description = "OK"),
//			@ApiResponse(responseCode = "404", description = "Cvu not found"),
//			@ApiResponse(responseCode = "400", description = "Transaction Error")
//	})
	public ResponseEntity<ResponseStatusWrapper<ExternalReceiveTransfer>> receiveTransfer(@RequestBody  @JsonView( Views.Public.class) ExternalReceiveTransfer externalTransfer) {
//		try {
			log.info("External bank recieve transfer");
			log.info("Request amount: "+externalTransfer.getAmount()+" - from:"+externalTransfer.getFromCvu()+" to:"+externalTransfer.getToCvu());
			var transfer = service.externalTransferReceive(externalTransfer);
			var response = new ResponseStatusWrapper<>(transfer,true,0,"Transfer completed");
//			return ResponseEntity.status(HttpStatus.CREATED).body(response);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
//		} catch (CvuException e) {
//			var response = new ResponseStatusWrapper<>(new EmptyResponse(),true,HttpStatus.NOT_FOUND.value(),e.getMessage());
////			//return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
//		} catch (TransactionException e) {
//			var response = new ResponseStatusWrapper<>(new EmptyResponse(),true,HttpStatus.BAD_REQUEST.value(),e.getMessage());
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); 
//		}
//		return null;
		
	}
	
	public void confirmTransfer() {
		
	}
}
