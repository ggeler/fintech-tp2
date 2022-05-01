package com.up.fintech.armagedon.tp4.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.up.fintech.armagedon.tp4.entity.User;
import com.up.fintech.armagedon.tp4.entity.Wallet;

@Repository
public interface IWalletRepository extends JpaRepository<Wallet, Long>{

	public Optional<Wallet> getWalletByUser(User user);

	public Optional<Wallet> getWalletByWalletId(UUID uuid);
}
