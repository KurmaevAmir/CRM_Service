package ru.itis.repositories;

import ru.itis.models.Passport;

import java.sql.SQLException;

public interface PassportRepository extends CrudRepository<Passport> {
    Passport findById(Long id) throws SQLException;
}
