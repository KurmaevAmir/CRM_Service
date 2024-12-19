package ru.itis.models;

import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class Employee extends Human {
    private String snils;
    private String inn;
    private Date date_employment;
    private List<Long> work;
}
