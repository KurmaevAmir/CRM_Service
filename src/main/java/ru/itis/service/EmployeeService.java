package ru.itis.service;

import ru.itis.dto.CRM.Employee.EmployeeDetailDto;
import ru.itis.dto.CRM.Employee.EmployeeListDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<EmployeeListDto> findByFullName(String name, String surname, String patronymic) throws SQLException;
    List<EmployeeListDto> findAll() throws SQLException;
    Optional<EmployeeDetailDto> findByEmail(String email) throws SQLException;
    void fire(String email) throws SQLException;
}
