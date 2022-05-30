package com.up.fintech.armagedon.tp4.service;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.Cvu;
import com.up.fintech.armagedon.tp4.entity.ExternalBank;
import com.up.fintech.armagedon.tp4.misc.error.ExternalBankException;
import com.up.fintech.armagedon.tp4.repository.IExternalBankRepository;

@Service
public class CvuService {

	private final IExternalBankRepository repository;
	
	public CvuService(IExternalBankRepository repository) {
		this.repository = repository;
	}
	
	public ExternalBank isExternalValid(String cvu) throws ExternalBankException {
		if (Cvu.isInternal(cvu))
			throw new ExternalBankException("Código de PSP es interno");
		var psp = Cvu.getPspCode(cvu);
		return repository.findByCvu(psp).orElseThrow(()->new ExternalBankException("Código de PSP Inexistente"));
	}
}
