package com.up.fintech.armagedon.tp4.controller.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.controller.TransactionController;
import com.up.fintech.armagedon.tp4.entity.Transaction;

@Service
public class TransactionAssembler implements RepresentationModelAssembler<Transaction, EntityModel<Transaction>> {

	private final PagedResourcesAssembler<Transaction> pagedAssembler;
	
	@Autowired
	public TransactionAssembler(PagedResourcesAssembler<Transaction> pagedAssembler) {
		this.pagedAssembler = pagedAssembler;
	}
	
	@Override
	public EntityModel<Transaction> toModel(Transaction entity) {
		EntityModel<Transaction> model;
		var transactionsLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TransactionController.class).getTransactionsPaged(entity.getWallet().getWalletId(),null)).withRel("transactions");
		model = EntityModel.of(entity, transactionsLink);
		
		return model;
	}
	
	public PagedModel<EntityModel<Transaction>> toModel(Page<Transaction> entity) {
		return pagedAssembler.toModel(entity);
	}
	
	@Override
	public CollectionModel<EntityModel<Transaction>> toCollectionModel(Iterable<? extends Transaction> entities) {
		
		return RepresentationModelAssembler.super.toCollectionModel(entities);
	}

}
