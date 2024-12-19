package ru.itis.repositories;

import ru.itis.models.SparePart;

import java.sql.SQLException;

public interface SparePartRepository extends CrudRepository<SparePart> {
    SparePart findByArticle(String article) throws SQLException;
    SparePart findByName(String name) throws SQLException;
    SparePart findById(int id) throws SQLException;
}
