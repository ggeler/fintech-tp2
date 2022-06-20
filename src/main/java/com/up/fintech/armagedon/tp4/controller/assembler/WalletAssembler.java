package com.up.fintech.armagedon.tp4.controller.assembler;

import java.math.BigDecimal;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.controller.BetController;
import com.up.fintech.armagedon.tp4.controller.DepositController;
import com.up.fintech.armagedon.tp4.controller.TransactionController;
import com.up.fintech.armagedon.tp4.controller.TransferController;
import com.up.fintech.armagedon.tp4.controller.WalletController;
import com.up.fintech.armagedon.tp4.controller.WithdrawController;
import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.state.wallet.WalletStatusEnum;

@Service
public class WalletAssembler implements RepresentationModelAssembler<Wallet, EntityModel<Wallet>> {

	@Override
	public EntityModel<Wallet> toModel(Wallet entity) {
		EntityModel<Wallet> model;
		var walletLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WalletController.class).getWallet(entity.getWalletId())).withSelfRel();
		var transactionlink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TransactionController.class).getTransactionsPaged(entity.getWalletId(),null)).withRel("transactions");
		var allBetsLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BetController.class).allBets(entity.getWalletId(),null,null)).withRel("allBets");
		
		model = EntityModel.of(entity, walletLink, transactionlink, allBetsLink);
		
		if (entity.getStatus()!=WalletStatusEnum.BLOCKED && entity.getStatus()!=WalletStatusEnum.BLOCKED_DEPOSIT ) {
			var depositLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DepositController.class).deposit(entity.getWalletId(),null)).withRel("deposit");
			model.add(depositLink);
		}
		if (entity.getStatus()!=WalletStatusEnum.BLOCKED &&  entity.getStatus()!=WalletStatusEnum.BLOCKED_WITHDRAW) {
			if (entity.getBalance().compareTo(BigDecimal.ZERO)>0) {
				var transferLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TransferController.class).transfer(entity.getWalletId(),null)).withRel("transfer");
				var withdrawLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WithdrawController.class).withdraw(entity.getWalletId(),null)).withRel("withdraw");
				var betLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BetController.class).preview(entity.getWalletId(),null)).withRel("bet");
				model.add(transferLink);
				model.add(withdrawLink);
				model.add(betLink);
			}
		}
		return model;
	}

}
