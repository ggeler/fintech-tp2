package com.up.fintech.armagedon.tp4.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp4.entity.bet.Event;
import com.up.fintech.armagedon.tp4.repository.IEventRepository;

@Service
public class EventService {

	private final IEventRepository repository;
	
	public EventService(IEventRepository repository) {
		this.repository = repository;
	}
	
	public Optional<Event> getEvent(long eventKey) {
		var event = repository.findByEventKey(eventKey);
		return event;
	}

	public Event save(Event event) {
		return repository.save(event);
	}
	
	public Optional<Event> saveOptional(Event event) {
		return Optional.of(repository.save(event));
	}
}
