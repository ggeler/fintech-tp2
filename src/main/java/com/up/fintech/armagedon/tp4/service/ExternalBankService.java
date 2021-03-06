package com.up.fintech.armagedon.tp4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Cvu;
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
		if (cvu == null || cvu.isEmpty() || cvu.isBlank() || cvu.length()<8) 
			throw new ExternalBankException("Invalid CVU");
		log.info("External CVU: "+cvu);
		log.info("Extenal Bank: "+Cvu.getPspCode(cvu));
		return Cvu.getPspCode(cvu);
	}
	
	public ExternalBank isExternalValid(String cvu) throws ExternalBankException {
		if (Cvu.isInternal(cvu))
			throw new ExternalBankException("Código de PSP es interno");
		var psp = Cvu.getPspCode(cvu);
		return repository.findByCvu(psp).orElseThrow(()->new ExternalBankException("Código de PSP Inexistente"));
	}
	
	public ExternalBank getExternalBank(String cvu) throws ExternalBankException {
		return repository.getBankByCvu(getBankCvuFromAccountCvu(cvu)).orElseThrow(() -> new ExternalBankException("External Bank not found"));
	}
}
