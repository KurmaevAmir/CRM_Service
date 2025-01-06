package ru.itis.models;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class Request {
    private Long id;
    private String description;
    private Timestamp date_creation;
    private Long status;
    private Long device;
    private Long client;
    private UUID identifier;
    private Long file;
}
