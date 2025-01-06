package ru.itis.service;

import ru.itis.dto.CRM.Manufacturer.ManufacturerBindingDto;
import ru.itis.dto.CRM.Specification.SpecificationListDto;
import ru.itis.models.Manufacturer;
import ru.itis.models.Specification;
import ru.itis.repositories.ManufacturerRepository;
import ru.itis.repositories.SpecificationRepository;

import java.sql.SQLException;
import java.util.List;

public class SpecificationServiceImpl implements SpecificationService {
    private SpecificationRepository specificationRepository;
    private ManufacturerRepository manufacturerRepository;

    public SpecificationServiceImpl(SpecificationRepository specificationRepository, ManufacturerRepository manufacturerRepository) {
        this.specificationRepository = specificationRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public void saveSpecification(String model, String article, String typeDeviceString, String manufacturerString) throws SQLException, NumberFormatException {
        Specification specification = new Specification();

        Long typeDevice = Long.parseLong(typeDeviceString);
        Long manufacturer = Long.parseLong(manufacturerString);

        specification.setModel(model);
        specification.setArticle(article);
        specification.setTypeDevice(typeDevice);
        specification.setManufacturer(manufacturer);
        specificationRepository.save(specification);
    }

    @Override
    public List<Manufacturer> findAllManufacturers() throws SQLException {
        return manufacturerRepository.findAll();
    }

    @Override
    public void bindingManufacturer(ManufacturerBindingDto manufacturerBindingDto) throws SQLException, NumberFormatException {
        Long manufacturerId = Long.parseLong(manufacturerBindingDto.getManufacturerId());
        Long typeDeviceId = Long.parseLong(manufacturerBindingDto.getDeviceTypeId());
        List<Long> typesDevice = new java.util.ArrayList<>();
        typesDevice.add(typeDeviceId);

        manufacturerRepository.binding(manufacturerId, typesDevice);
    }

    @Override
    public List<SpecificationListDto> findByManufacturerAndTypeDevice(Long manufacturerId, Long typeDeviceId) throws SQLException {
        return specificationRepository.findByManufacturerAndTypeDevice(manufacturerId, typeDeviceId);
    }

    @Override
    public List<SpecificationListDto> findAll() throws SQLException {
        return specificationRepository.findAllByDto();
    }

    @Override
    public void delete(Long id) throws SQLException {
        specificationRepository.delete(id);
    }
}
