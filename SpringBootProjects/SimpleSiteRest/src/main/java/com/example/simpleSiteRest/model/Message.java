package com.example.simpleSiteRest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.IdName.class)
    private Long id;
    @JsonView(Views.IdName.class)
    private String text;
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
    @Column(updatable = false)
    @JsonView(Views.FullName.class)
    private LocalDateTime creationDate;
}
