package ru.itis.repositories;

import ru.itis.dto.CRM.Employee.EmployeeDetailDto;
import ru.itis.dto.CRM.Employee.EmployeeListDto;
import ru.itis.models.Employee;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepositoryJdbcImpl implements EmployeeRepository{
    private final Connection connection;
    private final ManyToManyRelations m2m;

    private static final String SQL_SELECT_BY_ID = "select * from employee where id = ?";
    private static final String SQL_INSERT = "insert into employee (name, surname, patronymic, date_of_birth," +
            "phone_number, email, passport, snils, inn, date_employment, password)" +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, current_date, ?) returning id";
    private static final String SQL_SELECT_BY_ALL = "select * from employee";
    private static final String SQL_UPDATE = "update employee set name = ?, surname = ?, patronymic = ?, " +
            "date_of_birth = ?, phone_number = ?, email = ?, passport = ?, snils = ?, inn = ?, " +
            "date_employment = ?, password = ? where id = ?";
    private static final String SQL_DELETE = "delete from employee where id = ?";
    private static final String SQL_SELECT_BY_EMAIL = "select e.id, e.name, e.surname, e.patronymic, e.date_of_birth, " +
            "e.phone_number, e.email, e.date_employment, p.series, p.number, p.date_issue, p.issued, p.subdivision, " +
            "e.snils, e.inn from employee as e join passport as p on e.passport = p.id where e.email = ?";
    private static final String SQL_SELECT_COUNT_SNILS = "select count(*) from employee where snils = ?";
    private static final String SQL_SELECT_COUNT_INN = "select count(*) from employee where inn = ?";
    private static final String SQL_SELECT_BY_FULLNAME = "select e.name, e.surname, e.patronymic, e.phone_number, " +
            "e.email, e.date_employment from employee as e where name = ? and surname = ? and patronymic = ? " +
            "union select e.name, e.surname, e.patronymic, e.phone_number, e.email, e.date_employment " +
            "from employee as e where name = ? and surname = ?";

    public EmployeeRepositoryJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        System.out.println(preparedStatement.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            entity.setId(resultSet.getLong("id"));
        }
//        m2m.saveRelatedIds("workemployee", "work_id", "employee_id", entity.getId(), entity.getWork());
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
//        m2m.deleteRelatedIds("workemployee", "employee_id", entity.getId());
//        m2m.saveRelatedIds("workemployee", "work_id", "employee_id", entity.getId(), entity.getWork());
    }

    @Override
    public void delete(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, id);
        preparedStatement.execute();
    }

    @Override
    public Optional<EmployeeDetailDto> findByEmail(String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_EMAIL);
        preparedStatement.setString(1, email);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            EmployeeDetailDto dto = EmployeeDetailDto.builder()
                    .id(resultSet.getLong(1))
                    .name(resultSet.getString(2))
                    .surname(resultSet.getString(3))
                    .patronymic(resultSet.getString(4))
                    .dateBirth(resultSet.getDate(5))
                    .phoneNumber(resultSet.getString(6))
                    .email(resultSet.getString(7))
                    .dateEmployment(resultSet.getDate(8))
                    .passportSeries(resultSet.getString(9))
                    .passportNumber(resultSet.getString(10))
                    .passportIssueDate(resultSet.getDate(11))
                    .passportIssued(resultSet.getString(12))
                    .passportSubdivision(resultSet.getString(13))
                    .snils(resultSet.getString(14))
                    .inn(resultSet.getString(15))
                    .build();
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsBySnils(String snils) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_COUNT_SNILS);
        preparedStatement.setString(1, snils);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    @Override
    public boolean existsByInn(String inn) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_COUNT_INN);
        preparedStatement.setString(1, inn);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    @Override
    public List<EmployeeListDto> findByFullName(String name, String surname, String patronymic) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_FULLNAME);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, surname);
        preparedStatement.setString(3, patronymic);
        preparedStatement.setString(4, name);
        preparedStatement.setString(5, surname);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<EmployeeListDto> employees = new ArrayList<>();

        while (resultSet.next()) {
            EmployeeListDto dto = EmployeeListDto.builder()
                    .name(resultSet.getString(1))
                    .surname(resultSet.getString(2))
                    .patronymic(resultSet.getString(3))
                    .phoneNumber(resultSet.getString(4))
                    .email(resultSet.getString(5))
                    .dateOfEmployment(resultSet.getDate(6))
                    .build();
            employees.add(dto);
        }
        return employees;
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
        employee.setRequests(m2m.getRelatedIds("requestemployee", "request_id", "employee_id", employee.getId()));
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
