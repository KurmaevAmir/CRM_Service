package ru.itis.service;

import ru.itis.dto.CRM.Device.DeviceDto;
import ru.itis.models.Device;
import ru.itis.repositories.DeviceRepository;

import java.sql.SQLException;

public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void saveDevice(DeviceDto deviceDto) throws SQLException {
        Device device = new Device();
        device.setSerialNumber(deviceDto.getSerialNumber());
        device.setColor(deviceDto.getColor());
        device.setSpecification(deviceDto.getSpecificationId());

        deviceRepository.save(device);
    }
}
