package ru.itis.repositories;

import ru.itis.dto.CRM.Request.RequestDetailDto;
import ru.itis.dto.CRM.Request.RequestListDto;
import ru.itis.dto.RequestDto;
import ru.itis.models.Client;
import ru.itis.models.Request;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends CrudRepository<Request> {
    Optional<RequestDetailDto> findByUUID(UUID uuid) throws SQLException;
    Request findById(Long id) throws SQLException;
    Request findByClient(Client client) throws SQLException;
    List<RequestListDto> findByStatus(String status) throws SQLException;
    void updateStatus(UUID identifier, String status) throws SQLException;
    List<RequestListDto> findLikeIdentifier(String identifier) throws SQLException;
    Optional<Long> findByIdentifier(UUID identifier) throws SQLException;
    void bindWork(Long id, Long workId, Long employeeId) throws SQLException;
    List<RequestListDto> findByClient(Long clientId) throws SQLException;
    List<RequestListDto> findByEmployee(Long employeeId) throws SQLException;
}
