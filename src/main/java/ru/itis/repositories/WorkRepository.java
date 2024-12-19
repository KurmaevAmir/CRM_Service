package ru.itis.repositories;

import ru.itis.models.Work;

import java.sql.SQLException;

public interface WorkRepository extends CrudRepository<Work> {
    Work findById(Long id) throws SQLException;
    void addSparePart(Long workId, Long sparePartId) throws SQLException;
    void addEmployee(Long worid, Long employeeId) throws SQLException;
    void changePriceAndWarranty(Long workId, Integer newPrice, Integer newWarranty) throws SQLException;
}
