package ru.itis.repositories;

import ru.itis.models.TypeDevice;

import java.sql.SQLException;

public interface TypeDeviceRepository extends CrudRepository<TypeDevice> {
    TypeDevice getTypeDeviceById(Long id) throws SQLException;
}
