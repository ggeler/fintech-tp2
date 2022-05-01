package com.up.fintech.armagedon.tp4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.up.fintech.armagedon.tp4.entity.Deposit;

@Repository
public interface IDepositoRepositoryOLD extends JpaRepository<Deposit, Long> {

}
