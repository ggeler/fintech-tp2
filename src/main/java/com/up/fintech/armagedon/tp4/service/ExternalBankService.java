package com.up.fintech.armagedon.tp4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.ExternalBank;
import com.up.fintech.armagedon.tp4.misc.error.ExternalBankException;
import com.up.fintech.armagedon.tp4.repository.IExternalBankRepository;

@Service
public class ExternalBankService {
	
	private final IExternalBankRepository repository;
	
	@Autowired
	public ExternalBankService(IExternalBankRepository repository) {
		this.repository = repository;
	}

	private String getBankCvuFromAccountCvu(String cvu) {
		return cvu.substring(0, 4);
	}
	public ExternalBank getExternalBank(String cvu) throws ExternalBankException {
		
		return repository.getBankByCvu(getBankCvuFromAccountCvu(cvu)).orElseThrow(() -> new ExternalBankException("External Bank not found"));
	}
}
