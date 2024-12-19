package ru.itis.repositories;

import ru.itis.models.Specification;

import java.sql.SQLException;

public interface SpecificationRepository extends CrudRepository<Specification> {
    Specification findById(Long id) throws SQLException;
}
