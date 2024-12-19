package ru.itis.repositories;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {
    void save(T entity) throws SQLException;
    List<T> findAll() throws SQLException;
    void update(T entity) throws SQLException;
    void delete(T entity) throws SQLException;
}
