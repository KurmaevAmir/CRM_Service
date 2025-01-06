package ru.itis.service;

import ru.itis.models.TypeWork;
import ru.itis.repositories.TypeWorkRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TypeWorkServiceImpl implements TypeWorkService{
    private TypeWorkRepository typeWorkRepository;

    public TypeWorkServiceImpl(TypeWorkRepository typeWorkRepository) {
        this.typeWorkRepository = typeWorkRepository;
    }

    @Override
    public List<TypeWork> findAll() throws SQLException {
        return typeWorkRepository.findAll();
    }

    @Override
    public void fire(String typeWorkIdString) throws NumberFormatException, SQLException {
        Long typeWorkId = Long.parseLong(typeWorkIdString);
        typeWorkRepository.delete(typeWorkId);
    }

    @Override
    public Optional<TypeWork> findById(Long typeWorkId) throws SQLException {
        return typeWorkRepository.findById(typeWorkId);
    }

    @Override
    public void update(Long typeWork, String typeWorkOperation) throws SQLException {
        TypeWork typeWorkModel = new TypeWork(typeWork, typeWorkOperation);
        typeWorkRepository.update(typeWorkModel);
    }

    @Override
    public void save(String typeWorkOperation) throws SQLException {
        TypeWork typeWork = new TypeWork();
        typeWork.setOperation(typeWorkOperation);
        typeWorkRepository.save(typeWork);
    }
}
