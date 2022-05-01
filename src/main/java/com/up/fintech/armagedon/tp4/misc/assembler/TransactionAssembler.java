package com.up.fintech.armagedon.tp4.misc.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.controller.TransactionController;
import com.up.fintech.armagedon.tp4.entity.Transaction;

@Service
public class TransactionAssembler implements RepresentationModelAssembler<Transaction, EntityModel<Transaction>> {

	@Override
	public EntityModel<Transaction> toModel(Transaction entity) {
		EntityModel<Transaction> model;
		var transactionsLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TransactionController.class).getTransactions(null)).withRel("transactions");
		model = EntityModel.of(entity, transactionsLink);
		
		return model;
	}

}
