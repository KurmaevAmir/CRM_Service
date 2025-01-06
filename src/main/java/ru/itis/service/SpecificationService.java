package ru.itis.service;

import ru.itis.dto.CRM.Manufacturer.ManufacturerBindingDto;
import ru.itis.dto.CRM.Specification.SpecificationListDto;
import ru.itis.models.Manufacturer;

import java.sql.SQLException;
import java.util.List;

public interface SpecificationService {
    void saveSpecification(String model, String article, String typeDeviceString, String manufacturerString) throws SQLException, NumberFormatException;
    List<Manufacturer> findAllManufacturers() throws SQLException;
    void bindingManufacturer(ManufacturerBindingDto manufacturerBindingDto) throws SQLException, NumberFormatException;
    List<SpecificationListDto> findByManufacturerAndTypeDevice(Long manufacturerId, Long typeDeviceId) throws SQLException;
    List<SpecificationListDto> findAll() throws SQLException;
    void delete(Long id) throws SQLException;
}
