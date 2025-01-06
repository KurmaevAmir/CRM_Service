package ru.itis.repositories;

import ru.itis.dto.CRM.Employee.EmployeeDetailDto;
import ru.itis.dto.CRM.Employee.EmployeeListDto;
import ru.itis.models.Employee;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee> {
    Employee findById(int id) throws SQLException;
    Optional<EmployeeDetailDto> findByEmail(String email) throws SQLException;
    boolean existsBySnils(String snils) throws SQLException;
    boolean existsByInn(String inn) throws SQLException;
    List<EmployeeListDto> findByFullName(String name, String surname, String patronymic) throws SQLException;
}
