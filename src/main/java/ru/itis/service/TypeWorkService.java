package ru.itis.service;

import ru.itis.models.TypeWork;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TypeWorkService {
    List<TypeWork> findAll() throws SQLException;
    void fire(String typeWorkIdString) throws NumberFormatException, SQLException;
    Optional<TypeWork> findById(Long typeWorkId) throws SQLException;
    void update(Long typeWork, String typeWorkOperation) throws SQLException;
    void save(String typeWorkOperation) throws SQLException;
}
