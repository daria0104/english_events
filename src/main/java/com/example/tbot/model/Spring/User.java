package com.example.tbot.model.Spring;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.*;

@Entity(name = "users")
@Data
public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String userName;
//    private Timestamp registeredAt;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name="registered_users",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
//            inverseJoinColumns ={@JoinColumn(name = "event_id", referencedColumnName = "event_id")})
//    private List<Event> events = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
//                ", events=" + events +
                '}';
    }
}
