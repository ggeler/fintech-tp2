package com.up.fintech.armagedon.tp4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.up.fintech.armagedon.tp4.entity.bet.Event;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {

	Optional<Event> findByEventKey(long eventKey);

}
