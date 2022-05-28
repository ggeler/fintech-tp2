package com.up.fintech.armagedon.tp4.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.up.fintech.armagedon.tp4.entity.EmptyResponse;
import com.up.fintech.armagedon.tp4.entity.ResponseStatusWrapper;
import com.up.fintech.armagedon.tp4.misc.error.CvuException;
import com.up.fintech.armagedon.tp4.misc.error.ExternalBankException;
import com.up.fintech.armagedon.tp4.misc.error.TransactionException;
import com.up.fintech.armagedon.tp4.misc.error.UserNotFoundException;
import com.up.fintech.armagedon.tp4.misc.error.WalletAlreadyExistsException;
import com.up.fintech.armagedon.tp4.misc.error.WalletNotFoundException;

@RestControllerAdvice(assignableTypes = {CashController.class, ExternalTransferController.class, WalletController.class, TransferController.class, TransactionController.class})
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ExternalBankException.class, CvuException.class, WalletNotFoundException.class, UserNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ResponseStatusWrapper<EmptyResponse>> externalTransferException(RuntimeException ex) {
		var response = new ResponseStatusWrapper<>(new EmptyResponse(),true,HttpStatus.NOT_FOUND.value(),ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//		return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
	}
	
	@ExceptionHandler({TransactionException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseStatusWrapper<EmptyResponse>> transactionException(RuntimeException ex) {
		var response = new ResponseStatusWrapper<>(new EmptyResponse(),true,HttpStatus.BAD_REQUEST.value(),ex.getMessage());
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
	}
	
	@ExceptionHandler({WalletAlreadyExistsException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ResponseStatusWrapper<EmptyResponse>> duplicateWalletException(RuntimeException ex) {
		var response = new ResponseStatusWrapper<>(new EmptyResponse(),true,HttpStatus.CONFLICT.value(),ex.getMessage());
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
	}
}
