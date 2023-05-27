package com.example.telegramBot.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@Data
@Entity
@NoArgsConstructor
@ToString
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private boolean isBot;
    private String languageCode;
    private boolean isPremium;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    private String password;

    public User(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

}
