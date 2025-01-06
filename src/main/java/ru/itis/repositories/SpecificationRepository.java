package ru.itis.repositories;

import ru.itis.dto.CRM.Specification.SpecificationListDto;
import ru.itis.models.Specification;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface SpecificationRepository extends CrudRepository<Specification> {
    Optional<Specification> findById(Long id) throws SQLException;
    List<Specification> findByTypeDeviceManufacturer(Long manufacturerId, Long TypeDeviceId) throws SQLException;
    List<SpecificationListDto> findByManufacturerAndTypeDevice(Long manufacturerId, Long typeDeviceId) throws SQLException;
    List<SpecificationListDto> findAllByDto() throws SQLException;
}
