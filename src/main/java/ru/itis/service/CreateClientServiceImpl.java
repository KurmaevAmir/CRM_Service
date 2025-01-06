package ru.itis.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.dto.CreateClientForm;
import ru.itis.models.Client;
import ru.itis.models.Passport;
import ru.itis.repositories.ClientRepository;
import ru.itis.repositories.PassportRepository;
import ru.itis.repositories.UserRepository;

import java.sql.SQLException;

public class CreateClientServiceImpl implements CreateClientService {
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final PassportRepository passportRepository;
    private final UserRepository userRepository;

    public CreateClientServiceImpl(ClientRepository clientRepository, PassportRepository passportRepository, UserRepository userRepository) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.clientRepository = clientRepository;
        this.passportRepository = passportRepository;
        this.userRepository = userRepository;
    }


    @Override
    public String createClient(CreateClientForm form) throws SQLException {
        if (userRepository.existsByEmail(form.getEmail())) {
            return "Пользователь с таким email уже существует.";
        }

        if (passportRepository.existsByPassportSeriesAndPassportNumber(form.getPassportSeries(), form.getPassportNumber())) {
            return "Пользователь с такой серией и номером паспорта уже существует.";
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

        Client client = Client.builder()
                .name(form.getName())
                .surname(form.getSurname())
                .patronymic(form.getPatronymic())
                .date_of_birth(form.getDate_of_birth())
                .phone_number(form.getPhone_number())
                .email(form.getEmail())
                .passport(passportId)
                .password(passwordEncoder.encode(form.getPassword()))
                .build();

        clientRepository.save(client);

        return null;
    }
}
