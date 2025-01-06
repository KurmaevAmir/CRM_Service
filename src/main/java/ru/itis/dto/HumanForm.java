package ru.itis.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.sql.Date;

@Data
@SuperBuilder
public class HumanForm {
    private String name;
    private String surname;
    private String patronymic;
    private Date date_of_birth;
    private String phone_number;
    private String email;
    private String passportSeries;
    private String passportNumber;
    private Date passportIssueDate;
    private String passportIssued;
    private String passportSubdivision;
    private String password;
}
