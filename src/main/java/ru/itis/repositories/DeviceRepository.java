package ru.itis.repositories;

import ru.itis.models.Device;

import java.sql.SQLException;

public interface DeviceRepository extends CrudRepository<Device> {
    Device findById(Long id) throws SQLException;
}
