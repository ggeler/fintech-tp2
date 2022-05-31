package com.up.fintech.armagedon.tp4.controller.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.controller.CashController;
import com.up.fintech.armagedon.tp4.controller.TransactionController;
import com.up.fintech.armagedon.tp4.controller.TransferController;
import com.up.fintech.armagedon.tp4.controller.WalletController;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.state.wallet.WalletStatusEnum;

@Service
public class WalletAssembler implements RepresentationModelAssembler<Wallet, EntityModel<Wallet>> {

	@Override
	public EntityModel<Wallet> toModel(Wallet entity) {
		EntityModel<Wallet> model;
		var walletLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WalletController.class).getWallet(entity.getWalletId())).withSelfRel();
		var transactionlink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TransactionController.class).getTransactionsPaged(entity.getWalletId(),null)).withRel("transactions");
		
		model = EntityModel.of(entity, walletLink, transactionlink);
		
		if (entity.getStatus()!=WalletStatusEnum.BLOCKED || entity.getStatus()!=WalletStatusEnum.BLOCKED_RECEIVE || entity.getStatus()!=WalletStatusEnum.BLOCKED_SEND) {
			var depositLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CashController.class).depositMoney(entity.getWalletId(),null)).withRel("deposit");
			model.add(depositLink);
		}
		
		if (entity.getBalance()>0) {
			var transferLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TransferController.class).transferMoney(entity.getWalletId(),null)).withRel("transfer");
			var withdrawLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CashController.class).withdrawMoney()).withRel("withdraw");
			model.add(transferLink);
			model.add(withdrawLink);
		}
		
		return model;
	}

}
