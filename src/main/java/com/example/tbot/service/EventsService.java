package com.example.tbot.service;

import com.example.tbot.model.Spring.Event;
import com.example.tbot.model.Spring.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class EventsService {
    @Autowired
    private EventsRepository eventsRepository;

    public void saveEvent(Event event){
//        if(eventsRepository.findEventByName(event) != null)
//            return;
        eventsRepository.save(event);
    }
    public List<Event> showAll(){
        return (List<Event>) eventsRepository.findAll();
    }
}
