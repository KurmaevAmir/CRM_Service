package ru.itis.repositories;

import ru.itis.models.TypeDevice;

import java.sql.SQLException;
import java.util.Optional;

public interface TypeDeviceRepository extends CrudRepository<TypeDevice> {
    TypeDevice getTypeDeviceById(Long id) throws SQLException;
    Optional<Long> getIdByName(String name) throws SQLException;
}
