package ru.itis.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class RequestDto {
    private Long id;
    private String description;
    private Date dateCreation;
    private String status;
    private String device;
    private String client;
}