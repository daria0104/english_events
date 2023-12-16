package com.example.tbot;

import com.example.tbot.model.Spring.*;
import com.example.tbot.service.EventsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@SpringBootTest
class TBotApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private EventsService eventsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private RegisteredUsersRepository registeredUsersRepository;

    @Test
    void test(){
        Event event = eventsRepository.findEventByLevelAndTime("A", LocalTime.parse("13:00", DateTimeFormatter.ofPattern("HH:mm")));
        System.out.println(event);

        User user = userRepository.findUserByUserName("dasha_970");
        System.err.println(user);
        RegisteredUsers registeredUsers = new RegisteredUsers(user.getId(), user.getUserName(), event.getId(), event.getTime());
        System.out.println(registeredUsersRepository.existsByUserAndEvent(user.getId(), event.getId()));

//        Event event = eventsRepository.findEventById(1L);
        System.out.println(event);
//        eventsRepository.save(event);

        System.err.println(eventsService.showAll());
    }
    @Test
    void saveEventsA(){
        Event event = new Event("A", "I would like to...", "13:00");
        eventsService.saveEvent(event);
        eventsService.saveEvent(new Event("A", "Don't be shy", "15:00"));
        eventsService.saveEvent(new Event("A", "Is cooking easy?", "17:00"));
    }

    @Test
    void saveEventsB(){
        eventsService.saveEvent(new Event("B", "Response or answer", "13:00"));
        eventsService.saveEvent(new Event("B", "Social medias or Activity", "15:00"));
        eventsService.saveEvent(new Event("B", "You're in, aren't you?", "17:00"));
    }
}
