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

import com.up.fintech.armagedon.tp4.controller.BetController;
import com.up.fintech.armagedon.tp4.controller.DepositController;
import com.up.fintech.armagedon.tp4.controller.TransactionController;
import com.up.fintech.armagedon.tp4.controller.WithdrawController;
import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.TransactionType;
import com.up.fintech.armagedon.tp4.entity.credit.Deposit;
import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.entity.debit.Withdraw;
import com.up.fintech.armagedon.tp4.entity.state.transaction.TransactionStatusEnum;
import com.up.fintech.armagedon.tp4.entity.state.wallet.WalletStatusEnum;

@Service
public class TransactionAssembler implements RepresentationModelAssembler<Transaction, EntityModel<Transaction>> {

	private final PagedResourcesAssembler<EntityModel<Transaction>> pagedAssembler;
	
	@Autowired
	public TransactionAssembler(PagedResourcesAssembler<EntityModel<Transaction>> pagedAssembler) {
		this.pagedAssembler = pagedAssembler;
	}
	
	@Override
	public EntityModel<Transaction> toModel(Transaction entity) {
		EntityModel<Transaction> model;
		
		var selfLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TransactionController.class).getTransaction(entity.getWallet().getWalletId(), entity.getTransactionId())).withSelfRel();
		
		model = EntityModel.of(entity, selfLink);
		
		if (entity.getWallet().getStatus()!=WalletStatusEnum.BLOCKED && entity.getWallet().getStatus()!=WalletStatusEnum.BLOCKED_WITHDRAW 
				&& entity.getWallet().getStatus()!=WalletStatusEnum.CLOSED ) {
			if (entity.getType() == TransactionType.WITHDRAW && entity.getStatus() == TransactionStatusEnum.PENDING_CONFIRMATION) {
					var confirmLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WithdrawController.class).confirmWithdraw(entity.getWallet().getWalletId(),entity.getTransactionId(),((Withdraw) entity).getConfirmationCode())).withRel("confirm");
					var cancelLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WithdrawController.class).cancelWithdraw(entity.getWallet().getWalletId(),entity.getTransactionId(),((Withdraw) entity).getConfirmationCode())).withRel("cancel");
					var qrLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WithdrawController.class).getQrWithdraw(entity.getWallet().getWalletId(),entity.getTransactionId())).withRel("qr");
					model.add(confirmLink,cancelLink, qrLink);
			}
		}	
		if (entity.getWallet().getStatus()!=WalletStatusEnum.BLOCKED && entity.getWallet().getStatus()!=WalletStatusEnum.BLOCKED_DEPOSIT && 
				entity.getWallet().getStatus()!=WalletStatusEnum.CLOSED ) {
			if (entity.getType() == TransactionType.DEPOSIT && entity.getStatus() == TransactionStatusEnum.PENDING_CONFIRMATION) {
				var confirmLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DepositController.class).confirm(entity.getWallet().getWalletId(),entity.getTransactionId(),((Deposit) entity).getConfirmationCode())).withRel("confirm");
				var cancelLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DepositController.class).cancel(entity.getWallet().getWalletId(),entity.getTransactionId(),((Deposit) entity).getConfirmationCode())).withRel("cancel");
				var qrLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DepositController.class).getQrWithdraw(entity.getWallet().getWalletId(),entity.getTransactionId())).withRel("qr");
				model.add(confirmLink,cancelLink, qrLink);
			}
		}
		
		if (entity.getWallet().getStatus()!=WalletStatusEnum.BLOCKED && entity.getWallet().getStatus()!=WalletStatusEnum.BLOCKED_WITHDRAW && 
				entity.getWallet().getStatus()!=WalletStatusEnum.CLOSED ) {
			if (entity.getType() == TransactionType.BET && entity.getStatus() == TransactionStatusEnum.PENDING_CONFIRMATION) {
				var confirmLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BetController.class).confirm(entity.getWallet().getWalletId(),entity.getTransactionId(),((Bet) entity).getConfirmationCode())).withRel("confirm");
				var cancelLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BetController.class).cancel(entity.getWallet().getWalletId(),entity.getTransactionId(),((Bet) entity).getConfirmationCode())).withRel("cancel");
				model.add(confirmLink,cancelLink);
			}
		}
		return model;
	}
	
	public PagedModel<EntityModel<EntityModel<Transaction>>> toModel(Page<EntityModel<Transaction>> entity) {
		return pagedAssembler.toModel(entity);
	}
	
	@Override
	public CollectionModel<EntityModel<Transaction>> toCollectionModel(Iterable<? extends Transaction> entities) {
		return RepresentationModelAssembler.super.toCollectionModel(entities);
	}

}
