package ru.itis.repositories;

import ru.itis.dto.HumanDto;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository{
    Optional<HumanDto> findByEmailAndPassword(String email, String password) throws SQLException;
    boolean existsByEmail(String email) throws SQLException;
}
