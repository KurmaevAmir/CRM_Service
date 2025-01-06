package ru.itis.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ValidateDto {
    private String name;
    private String surname;
    private String patronymic;
    private String date_of_birth;
    private String phone_number;
    private String email;
    private String passportSeries;
    private String passportNumber;
    private String passportIssueDate;
    private String passportIssued;
    private String passportSubdivision;
    private String password;
    private String passwordConfirm;
    private String snils;
    private String inn;
}
