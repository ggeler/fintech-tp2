package com.up.fintech.armagedon.tp4.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.up.fintech.armagedon.tp4.entity.Wallet;
import com.up.fintech.armagedon.tp4.entity.debit.Bet;
import com.up.fintech.armagedon.tp4.entity.state.transaction.TransactionStatusEnum;

@Repository
public interface IBetRepository extends JpaRepository<Bet, Long>{

	Page<Bet> findAllByWallet(Wallet Wallet, Pageable pageable);

	Page<Bet> findAllByWalletAndStatus(Wallet wallet, TransactionStatusEnum completed, Pageable pageable);

	Page<Bet> findAllByWalletAndStatus(Wallet wallet, String status, Pageable pageable);
}
