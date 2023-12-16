package com.example.tbot.model.Spring;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Objects;

@Entity(name = "registered_users")
@NoArgsConstructor
@Data
public class RegisteredUsers {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long registered_id;
    Long user;
    String username;

    Long event;
    LocalTime time;

    public RegisteredUsers(Long user, String username, Long event, LocalTime time) {
        this.registered_id = 0L;
        this.user = user;
        this.username = username;
        this.event = event;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisteredUsers that = (RegisteredUsers) o;
        return Objects.equals(user, that.user) && Objects.equals(username, that.username) && Objects.equals(event, that.event) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, username, event, time);
    }
}
