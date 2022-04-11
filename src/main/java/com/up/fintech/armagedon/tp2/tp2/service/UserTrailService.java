package com.up.fintech.armagedon.tp2.tp2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp2.tp2.dao.UserTrail;
import com.up.fintech.armagedon.tp2.tp2.repository.IUserTrailRepository;

@Service
public class UserTrailService {

	private final IUserTrailRepository repository;

	@Autowired
	public UserTrailService(IUserTrailRepository repository) {
		this.repository = repository;
	}

	public void log(UserTrail userTrail) {
		repository.save(userTrail);		
	}

	public List<UserTrail> getLog() {
		return repository.findAll();
	}
}
