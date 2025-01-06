package ru.itis.service;

import ru.itis.models.TypeDevice;

import java.sql.SQLException;
import java.util.List;

public interface TypeDeviceService {
    void save(String name) throws SQLException;
    List<TypeDevice> findAll() throws SQLException;
    void delete(String id) throws SQLException;
}
