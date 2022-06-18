package com.up.fintech.armagedon.tp4.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.up.fintech.armagedon.tp4.entity.Transaction;
import com.up.fintech.armagedon.tp4.entity.Wallet;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long>{

	Page<Transaction> findAllByWallet(Wallet wallet, Pageable pageable);
	Optional<Transaction> findByTransactionId(UUID transaction);
}
