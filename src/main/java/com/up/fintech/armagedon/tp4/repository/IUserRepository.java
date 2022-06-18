package com.up.fintech.armagedon.tp4.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.up.fintech.armagedon.tp4.entity.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	public Optional<User> getUserByUuid(UUID uuid);
	public Optional<User> getUserByEmail(String email);
	public Optional<User> getUserByCuit(String cuit);
	public Optional<User> getUserByUuidAndEmailAndCuit(UUID uuid, String email, String cuit);
	public Optional<User> getUserByUuidOrEmailOrCuit(UUID uuid, String email, String cuit);
}
