package ru.itis.service;

import ru.itis.dto.CRM.Work.WorkListDto;
import ru.itis.models.Device;
import ru.itis.models.TypeWork;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface WorkService {
    List<TypeWork> findAllTypesWork() throws SQLException;
    Optional<Long> validateTypeWorkOperation(String typeWorkOperation) throws SQLException;
    Optional<Long> validateSpecification(String specificationIdStr) throws SQLException;
    void saveWork(Long typeWork, String priceStr, String warrantyStr, Long specification) throws NumberFormatException, SQLException;
    List<WorkListDto> findBySpecification(Long specificationId) throws SQLException;
    List<WorkListDto> findAllWorks() throws SQLException;
    void fire(String workId) throws SQLException, NumberFormatException;
}
