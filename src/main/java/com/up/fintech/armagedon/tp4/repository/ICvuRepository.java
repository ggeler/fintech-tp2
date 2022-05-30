package com.up.fintech.armagedon.tp4.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.up.fintech.armagedon.tp4.entity.Cvu;

@Repository
public interface ICvuRepository extends JpaRepository<Cvu, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public Optional<Cvu> getCvuByCvu(String toCvu);

}
