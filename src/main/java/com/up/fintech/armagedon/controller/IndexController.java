package com.up.fintech.armagedon.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.up.fintech.armagedon.tp1.controller.EchoTestController;
import com.up.fintech.armagedon.tp2.controller.LogController;
import com.up.fintech.armagedon.tp3.controller.CryptoController;
import com.up.fintech.armagedon.tp4.controller.WalletController;

@RestController
@RequestMapping("/")
public class IndexController {
	
	static class CustomRepresentationModel extends RepresentationModel<CustomRepresentationModel> {
		public CustomRepresentationModel(Iterable<Link> initialLinks) {
			super(initialLinks);
		}
	}
	
	@GetMapping 
	public CustomRepresentationModel index() {
		
		List<Link> initialLinks = new ArrayList<>();
		
		initialLinks.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(WalletController.class).getWalletByUser(null)).withRel("wallet"));
		initialLinks.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EchoTestController.class).index()).withRel("test"));
		initialLinks.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(LogController.class).getLog()).withRel("logs"));
		initialLinks.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(LogController.class).getLogPaged(null)).withRel("paged_logs"));
		initialLinks.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CryptoController.class).getPublicKey()).withRel("publickey"));
		initialLinks.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CryptoController.class).getSecretKey(null)).withRel("secretkey"));
				
		return new CustomRepresentationModel(initialLinks);
	}
}
