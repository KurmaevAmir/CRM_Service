package ru.itis.service;

import ru.itis.dto.CRM.Employee.EmployeeDetailDto;
import ru.itis.dto.CRM.Employee.EmployeeListDto;
import ru.itis.models.Employee;
import ru.itis.repositories.EmployeeRepository;
import ru.itis.repositories.PassportRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private PassportRepository passportRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PassportRepository passportRepository) {
        this.employeeRepository = employeeRepository;
        this.passportRepository = passportRepository;
    }

    @Override
    public List<EmployeeListDto> findByFullName(String name, String surname, String patronymic) throws SQLException {
        return employeeRepository.findByFullName(name, surname, patronymic);
    }

    @Override
    public List<EmployeeListDto> findAll() throws SQLException {
        List<EmployeeListDto> employeeListDto = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            EmployeeListDto dto = EmployeeListDto.builder()
                    .name(employee.getName())
                    .surname(employee.getSurname())
                    .patronymic(employee.getPatronymic())
                    .phoneNumber(employee.getPhone_number())
                    .email(employee.getEmail())
                    .dateOfEmployment(employee.getDate_employment())
                    .build();
            employeeListDto.add(dto);
        }
        return employeeListDto;
    }

    @Override
    public Optional<EmployeeDetailDto> findByEmail(String email) throws SQLException {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public void fire(String email) throws SQLException {
        Optional<EmployeeDetailDto> employeeOptional = employeeRepository.findByEmail(email);
        if (employeeOptional.isPresent()) {
            EmployeeDetailDto employee = employeeOptional.get();
            Long idPassport = passportRepository.findIdBySeriesNumber(employee.getPassportSeries(), employee.getPassportNumber());
            passportRepository.delete(idPassport);
            employeeRepository.delete(employee.getId());
        }
    }
}
