package ru.itis.repositories;

import ru.itis.dto.CRM.Work.WorkAddDto;
import ru.itis.dto.CRM.Work.WorkListDto;
import ru.itis.models.Work;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface WorkRepository extends CrudRepository<Work> {
    Work findById(Long id) throws SQLException;
    void addSparePart(Long workId, Long sparePartId) throws SQLException;
    void addEmployee(Long worid, Long employeeId) throws SQLException;
    void changePriceAndWarranty(Long workId, Integer newPrice, Integer newWarranty) throws SQLException;
    List<WorkListDto> findBySpecification(Long specificationId) throws SQLException;
    List<WorkListDto> findAllWorkListDto() throws SQLException;
    List<WorkListDto> findByRequestIdentifier(UUID identifier) throws SQLException;
    void deleteBindRequest(Long request, Long workId) throws SQLException;
    List<WorkAddDto> findTypesWorkByModel(String model) throws SQLException;
}
