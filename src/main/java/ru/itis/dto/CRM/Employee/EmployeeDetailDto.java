package ru.itis.dto.CRM.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailDto {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private Date dateBirth;
    private String phoneNumber;
    private String email;
    private Date dateEmployment;
    private String passportSeries;
    private String passportNumber;
    private Date passportIssueDate;
    private String passportIssued;
    private String passportSubdivision;
    private String snils;
    private String inn;
}
