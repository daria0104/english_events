package com.example.tbot.model.Spring;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity(name = "event")
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "event_id")
    private Long id;
    private String level;
    private String name;
    private LocalTime time;
//    @ManyToMany(mappedBy = "events")
//    private List<User> users;

    public Event(String level, String name, String time) {
        id = 0L;
        this.level = level;
        this.name = name;
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));//LocalDateTime.parse(time, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", level='" + level + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
//                ", users=" + users +
                '}';
    }
}
