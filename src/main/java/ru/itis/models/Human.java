package ru.itis.models;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
public class Human {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private Date date_of_birth;
    private String phone_number;
    private String email;
    private Long passport;
    private String password;
}
