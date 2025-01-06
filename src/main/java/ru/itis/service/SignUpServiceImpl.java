package ru.itis.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.dto.SignUpEmployeeForm;
import ru.itis.models.Employee;
import ru.itis.models.Passport;
import ru.itis.repositories.EmployeeRepository;
import ru.itis.repositories.PassportRepository;
import ru.itis.repositories.UserRepository;

import java.sql.SQLException;

public class SignUpServiceImpl implements SignUpService {
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final PassportRepository passportRepository;
    private final UserRepository userRepository;

    public SignUpServiceImpl(EmployeeRepository employeeRepository, PassportRepository passportRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.passportRepository = passportRepository;
        passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
    }

    @Override
    public String signUp(SignUpEmployeeForm form) throws SQLException {
        if (userRepository.existsByEmail(form.getEmail())) {
            return "Пользователь с таким email уже существует.";
        }

        if (passportRepository.existsByPassportSeriesAndPassportNumber(form.getPassportSeries(), form.getPassportNumber())) {
            return "Пользователь с такой серией и номером паспорта уже существует.";
        }

        if (employeeRepository.existsBySnils(form.getSnils())) {
            return "Пользователь с таким СНИЛС уже существует.";
        }

        if (employeeRepository.existsByInn(form.getInn())) {
            return "Пользователь с таким ИНН уже существует.";
        }

        Passport passport = Passport.builder()
                .series(form.getPassportSeries())
                .number(form.getPassportNumber())
                .date_issue(form.getPassportIssueDate())
                .issued(form.getPassportIssued())
                .subdivision(form.getPassportSubdivision())
                .build();

        passportRepository.save(passport);

        Long passportId = passportRepository.findIdBySeriesNumber(passport.getSeries(), passport.getNumber());

        Employee employee = Employee.builder()
                .name(form.getName())
                .surname(form.getSurname())
                .patronymic(form.getPatronymic())
                .date_of_birth(form.getDate_of_birth())
                .phone_number(form.getPhone_number())
                .email(form.getEmail())
                .passport(passportId)
                .password(passwordEncoder.encode(form.getPassword()))
                .snils(form.getSnils())
                .inn(form.getInn())
                .build();

        employeeRepository.save(employee);

        return null;
    }
}
