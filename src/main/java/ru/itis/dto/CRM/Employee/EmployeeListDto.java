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
public class EmployeeListDto {
    private String name;
    private String surname;
    private String patronymic;
    private String phoneNumber;
    private String email;
    private Date dateOfEmployment;
}
