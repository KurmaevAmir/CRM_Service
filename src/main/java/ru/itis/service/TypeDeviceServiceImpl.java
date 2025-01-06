package ru.itis.service;

import ru.itis.models.TypeDevice;
import ru.itis.repositories.TypeDeviceRepository;

import java.sql.SQLException;
import java.util.List;

public class TypeDeviceServiceImpl implements TypeDeviceService {
    private TypeDeviceRepository typeDeviceRepository;

    public TypeDeviceServiceImpl(TypeDeviceRepository typeDeviceRepository) {
        this.typeDeviceRepository = typeDeviceRepository;
    }

    @Override
    public void save(String name) throws SQLException {
        TypeDevice typeDevice = new TypeDevice();
        typeDevice.setName(name);
        typeDeviceRepository.save(typeDevice);
    }

    @Override
    public List<TypeDevice> findAll() throws SQLException {
        return typeDeviceRepository.findAll();
    }

    @Override
    public void delete(String id) throws SQLException {
        Long typeDeviceId = Long.parseLong(id);
        typeDeviceRepository.delete(typeDeviceId);
    }
}
