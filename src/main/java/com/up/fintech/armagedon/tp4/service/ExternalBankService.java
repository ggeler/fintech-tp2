package com.up.fintech.armagedon.tp4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.ExternalBank;
import com.up.fintech.armagedon.tp4.misc.error.ExternalBankException;
import com.up.fintech.armagedon.tp4.repository.IExternalBankRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ExternalBankService {
	
	private final IExternalBankRepository repository;
	
	@Autowired
	public ExternalBankService(IExternalBankRepository repository) {
		this.repository = repository;
	}

	private String getBankCvuFromAccountCvu(String cvu) {
		log.info("External CVU: "+cvu);
		log.info("Extenal Bank: "+cvu.substring(0, 6));
		return cvu.substring(0, 7);
	}
	public ExternalBank getExternalBank(String cvu) throws ExternalBankException {
		return repository.getBankByCvu(getBankCvuFromAccountCvu(cvu)).orElseThrow(() -> new ExternalBankException("External Bank not found"));
	}
}
