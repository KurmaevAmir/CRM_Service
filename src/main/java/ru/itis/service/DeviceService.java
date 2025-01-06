package ru.itis.service;

import ru.itis.dto.CRM.Device.DeviceDto;

import java.sql.SQLException;

public interface DeviceService {
    void saveDevice(DeviceDto deviceDto) throws SQLException;
}
