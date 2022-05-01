package com.up.fintech.armagedon.tp4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.up.fintech.armagedon.tp4.entity.Cvu;

@Repository
public interface ICvuRepository extends JpaRepository<Cvu, Long> {

	public Optional<Cvu> getCvuByCvu(String toCvu);

}
