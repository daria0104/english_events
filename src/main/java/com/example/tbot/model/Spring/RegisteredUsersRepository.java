package com.example.tbot.model.Spring;

import org.springframework.data.repository.CrudRepository;

public interface RegisteredUsersRepository extends CrudRepository<RegisteredUsers, Long> {
    boolean existsByUserAndEvent(Long event_id, Long user_id);
}
