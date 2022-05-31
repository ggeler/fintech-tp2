package com.up.fintech.armagedon.tp4.controller.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.controller.ExternalTransferWithConfirmationController;
import com.up.fintech.armagedon.tp4.entity.Transaction;

@Service
public class ExternalReceiveTransferWithConfirmationAssembler implements RepresentationModelAssembler<Transaction, EntityModel<Transaction>> {
	
	@Override
	public EntityModel<Transaction> toModel(Transaction entity) {
		EntityModel<Transaction> model;
		var transaction = entity.getTransactionId();
		var wallet = entity.getWallet().getWalletId();
		var confirmLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExternalTransferWithConfirmationController.class).confirmTransfer(wallet,transaction,null)).withRel("confirm");
		var cancelLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExternalTransferWithConfirmationController.class).cancelTransfer(wallet,transaction,null)).withRel("cancel");
		model = EntityModel.of(entity, confirmLink, cancelLink);
		return model;
	}
	
}
