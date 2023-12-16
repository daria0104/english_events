package com.example.tbot.model.Spring;

//import com.example.tbot.model.Spring.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserById(Long id);
    User findUserByUserName(String username);
}
