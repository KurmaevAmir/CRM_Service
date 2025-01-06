package ru.itis.service;

import ru.itis.dto.CRM.Manufacturer.ManufacturerAddAjaxDto;
import ru.itis.models.Manufacturer;
import ru.itis.repositories.ManufacturerRepository;
import ru.itis.repositories.TypeDeviceRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManufacturerServiceImpl implements ManufacturerService {
    private ManufacturerRepository manufacturerRepository;
    private TypeDeviceRepository typeDeviceRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, TypeDeviceRepository typeDeviceRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.typeDeviceRepository = typeDeviceRepository;
    }

    @Override
    public void saveManufacturer(ManufacturerAddAjaxDto manufacturerAddAjaxDto) throws SQLException, NumberFormatException {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(manufacturerAddAjaxDto.getManufacturerName());
        Long typeDeviceId;
        typeDeviceId = Long.parseLong(manufacturerAddAjaxDto.getDeviceType());
        List<Long> typesDevice = new ArrayList<>();
        typesDevice.add(typeDeviceId);
        manufacturer.setTypesDevice(typesDevice);
        manufacturerRepository.save(manufacturer);
    }

    @Override
    public List<Manufacturer> findAll() throws SQLException {
        return manufacturerRepository.findAll();
    }

    @Override
    public void delete(Long manufacturerId) throws SQLException {
        manufacturerRepository.delete(manufacturerId);
    }
}
