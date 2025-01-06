package ru.itis.service;

import ru.itis.dto.CRM.Client.ClientAjaxDto;
import ru.itis.dto.CRM.Device.DeviceAjaxDto;
import ru.itis.dto.CRM.Request.*;
import ru.itis.dto.CRM.Work.WorkAddDto;
import ru.itis.dto.CRM.Work.WorkListDto;
import ru.itis.models.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestService {
    List<Client> getAllClients() throws SQLException;
    List<Client> getClients(ClientAjaxDto form) throws SQLException;
    List<TypeDevice> getAllDevices() throws SQLException;
    List<Manufacturer> getManufacturers(Long id) throws SQLException;
    List<Specification> getSpecifications(Long manufacturerId, Long typeDeviceId) throws SQLException;
    Device getDevice(DeviceAjaxDto deviceDto) throws SQLException;
    void saveRequest(CreateRequestDto requestDto) throws SQLException;
    List<RequestListDto> findAllByStatus(String status) throws SQLException;
    List<RequestListDto> findAllByIdentifier(String identifier) throws SQLException;
    Optional<UUID> validateDetailRequest(String uuid);
    Optional<RequestDetailDto> findByIdentifier(UUID identifier) throws SQLException;
    void updateStatus(UUID identifier, String status) throws SQLException;
    List<WorkListDto> findWorksByIdentifier(UUID identifier) throws SQLException;
    void deleteBind(String identifierStr, String workIdString) throws SQLException, IllegalArgumentException;
    List<WorkAddDto> findWorkByModel(String model) throws SQLException;
    void bindWork(String identifierStr, String workIdString, String email) throws SQLException, IllegalArgumentException;
    List<Status> getAllStatuses() throws SQLException;
}
