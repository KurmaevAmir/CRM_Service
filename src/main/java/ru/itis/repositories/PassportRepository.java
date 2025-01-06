package ru.itis.repositories;

import ru.itis.models.Passport;

import java.sql.SQLException;

public interface PassportRepository extends CrudRepository<Passport> {
    Passport findById(Long id) throws SQLException;
    Long findIdBySeriesNumber(String series, String number) throws SQLException;
    boolean existsByPassportSeriesAndPassportNumber(String series, String number) throws SQLException;
}
