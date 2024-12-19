package ru.itis.repositories;

import ru.itis.models.Employee;

import java.sql.SQLException;

public interface EmployeeRepository extends CrudRepository<Employee> {
    Employee findById(int id) throws SQLException;
}
