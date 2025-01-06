package ru.itis.repositories;

import ru.itis.models.TypeWork;

import java.sql.SQLException;
import java.util.Optional;

public interface TypeWorkRepository extends CrudRepository<TypeWork> {
    Optional<TypeWork> findById(Long id) throws SQLException;
    Optional<Long> findByOperation(String operation) throws SQLException;
}
