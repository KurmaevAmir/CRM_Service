package ru.itis.service;

import ru.itis.dto.SignUpEmployeeForm;

import java.sql.SQLException;

public interface SignUpService {
    String signUp(SignUpEmployeeForm form) throws SQLException;
}
