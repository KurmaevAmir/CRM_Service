package ru.itis.repositories;

import ru.itis.dto.CRM.Manufacturer.ManufacturerAddAjaxDto;
import ru.itis.models.Manufacturer;

import java.sql.SQLException;
import java.util.List;

public interface ManufacturerRepository {
    List<Manufacturer> findByTypeDevice(Long id) throws SQLException;
    Manufacturer findById(Long id) throws SQLException;
    void save(Manufacturer manufacturer) throws SQLException;
    Long findIdByName(String name) throws SQLException;
    List<Manufacturer> findAll() throws SQLException;
    void binding(Long id, List<Long> typeDeviceId) throws SQLException;
    void delete(Long id) throws SQLException;
}
