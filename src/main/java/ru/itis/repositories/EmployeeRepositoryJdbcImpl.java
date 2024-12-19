package ru.itis.repositories;

import ru.itis.models.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryJdbcImpl implements EmployeeRepository{
    private final Connection connection;
    private final ManyToManyRelations m2m;

    private static final String SQL_SELECT_BY_ID = "select * from employee where id = ?";
    private static final String SQL_INSERT = "insert into employee (name, surname, patronymic, date_of_birth," +
            "phone_number, email, passport, snils, inn, date_employment, password)" +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, current_date, ?)";
    private static final String SQL_SELECT_BY_ALL = "select * from employee";
    private static final String SQL_UPDATE = "update employee set name = ?, surname = ?, patronymic = ?, " +
            "date_of_birth = ?, phone_number = ?, email = ?, passport = ?, snils = ?, inn = ?, " +
            "date_employment = ?, password = ? where id = ?";
    private static final String SQL_DELETE = "delete from employee where id = ?";

    EmployeeRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
        this.m2m = new ManyToManyRelations(connection);
    }

    @Override
    public Employee findById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createEmployee(resultSet);
        }
        return null;
    }

    @Override
    public void save(Employee entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        prepareStatement(preparedStatement, entity);
        preparedStatement.setString(10, entity.getPassword());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            entity.setId(resultSet.getLong("id"));
        }
        m2m.saveRelatedIds("workemployy", "work_id", "employee_id", entity.getId(), entity.getWork());
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_BY_ALL);
        List<Employee> employees = new ArrayList<>();
        while (resultSet.next()) {
            employees.add(createEmployee(resultSet));
        }
        return employees;
    }

    @Override
    public void update(Employee entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        prepareStatement(preparedStatement, entity);
        preparedStatement.setDate(10, entity.getDate_employment());
        preparedStatement.setString(11, entity.getPassword());
        preparedStatement.setLong(12, entity.getId());
        preparedStatement.executeUpdate();
        m2m.deleteRelatedIds("workemployee", "employee_id", entity.getId());
        m2m.saveRelatedIds("workemployee", "work_id", "employee_id", entity.getId(), entity.getWork());
    }

    @Override
    public void delete(Employee entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.execute();
    }

    private Employee createEmployee(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getLong(1));
        employee.setName(resultSet.getString(2));
        employee.setSurname(resultSet.getString(3));
        employee.setPatronymic(resultSet.getString(4));
        employee.setDate_of_birth(resultSet.getDate(5));
        employee.setPhone_number(resultSet.getString(6));
        employee.setEmail(resultSet.getString(7));
        employee.setPassport(resultSet.getLong(8));
        employee.setSnils(resultSet.getString(9));
        employee.setInn(resultSet.getString(10));
        employee.setDate_employment(resultSet.getDate(11));
        employee.setPassword(resultSet.getString(12));
        employee.setWork(m2m.getRelatedIds("workemployee", "work_id", "employee_id", employee.getId()));
        return employee;
    }

    private void prepareStatement(PreparedStatement preparedStatement, Employee entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getSurname());
        preparedStatement.setString(3, entity.getPatronymic());
        preparedStatement.setDate(4, entity.getDate_of_birth());
        preparedStatement.setString(5, entity.getPhone_number());
        preparedStatement.setString(6, entity.getEmail());
        preparedStatement.setLong(7, entity.getPassport());
        preparedStatement.setString(8, entity.getSnils());
        preparedStatement.setString(9, entity.getInn());
    }
}
