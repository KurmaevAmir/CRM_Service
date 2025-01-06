package ru.itis.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SignUpEmployeeForm extends HumanForm {
    private String snils;
    private String inn;
}
