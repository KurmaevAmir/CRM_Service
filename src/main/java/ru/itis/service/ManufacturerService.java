package ru.itis.service;

import ru.itis.dto.CRM.Manufacturer.ManufacturerAddAjaxDto;
import ru.itis.models.Manufacturer;

import java.sql.SQLException;
import java.util.List;

public interface ManufacturerService {
    void saveManufacturer(ManufacturerAddAjaxDto manufacturerAddAjaxDto) throws SQLException, NumberFormatException;
    List<Manufacturer> findAll() throws SQLException;
    void delete(Long manufacturerId) throws SQLException;
}
