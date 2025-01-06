package ru.itis.service;

import ru.itis.dto.HumanDto;

import java.util.Optional;

public interface AuthService {
    Optional<HumanDto> getHumanDto(String email, String password);
}
