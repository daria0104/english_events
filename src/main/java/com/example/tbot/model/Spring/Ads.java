package com.example.tbot.model.Spring;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "AdsTable")
@Getter
@Setter
public class Ads {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ad_id;
    private String ad;
}
