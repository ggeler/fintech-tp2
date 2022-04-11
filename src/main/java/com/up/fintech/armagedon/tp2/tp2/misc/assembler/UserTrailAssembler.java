package com.up.fintech.armagedon.tp2.tp2.misc.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.up.fintech.armagedon.tp2.tp2.entity.UserTrail;

@Component
public class UserTrailAssembler implements RepresentationModelAssembler<UserTrail, EntityModel<UserTrail>> {

	private final PagedResourcesAssembler<UserTrail> pagedAssembler;

	@Autowired
	public UserTrailAssembler(PagedResourcesAssembler<UserTrail> pagedAssembler) {
		this.pagedAssembler = pagedAssembler;
	}
	@Override
	public EntityModel<UserTrail> toModel(UserTrail entity) {
		return null;
	}
	
	public PagedModel<EntityModel<UserTrail>> toModel(Page<UserTrail> entity) {
		return pagedAssembler.toModel(entity);
	}

}
