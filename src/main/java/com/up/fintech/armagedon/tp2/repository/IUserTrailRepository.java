package com.up.fintech.armagedon.tp2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.up.fintech.armagedon.tp2.entity.UserTrail;

@Repository
public interface IUserTrailRepository extends JpaRepository<UserTrail, Long> { 

}
