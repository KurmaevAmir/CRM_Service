package ru.itis.service;

import ru.itis.dto.HumanDto;
import ru.itis.repositories.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<HumanDto> getHumanDto(String email, String password) {
        try {
            return userRepository.findByEmailAndPassword(email, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
