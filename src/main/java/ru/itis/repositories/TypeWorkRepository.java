package ru.itis.repositories;

import ru.itis.models.TypeWork;

import java.sql.SQLException;

public interface TypeWorkRepository extends CrudRepository<TypeWork> {
    TypeWork findById(Long id) throws SQLException;
}
