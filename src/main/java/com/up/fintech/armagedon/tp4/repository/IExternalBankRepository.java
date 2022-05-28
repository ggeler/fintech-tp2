package com.up.fintech.armagedon.tp4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.up.fintech.armagedon.tp4.entity.ExternalBank;

@Repository
public interface IExternalBankRepository extends JpaRepository<ExternalBank, Long>{

	public Optional<ExternalBank> getBankByCvu(String cvu);

}
