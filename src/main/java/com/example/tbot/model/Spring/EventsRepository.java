package com.example.tbot.model.Spring;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface EventsRepository extends CrudRepository<Event, Long> {
    Event findEventByName(Event event);
    Event findEventById(Long id);

    Event findEventByLevelAndTime(String level, LocalTime time);
}
