package ru.itis.service;

import ru.itis.dto.CRM.Client.ClientAjaxDto;
import ru.itis.dto.CRM.Device.DeviceAjaxDto;
import ru.itis.dto.CRM.Employee.EmployeeDetailDto;
import ru.itis.dto.CRM.Request.*;
import ru.itis.dto.CRM.Work.WorkAddDto;
import ru.itis.dto.CRM.Work.WorkListDto;
import ru.itis.models.*;
import ru.itis.repositories.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RequestServiceImpl implements RequestService{
    private ClientRepository clientRepository;
    private TypeDeviceRepository typeDeviceRepository;
    private ManufacturerRepository manufacturerRepository;
    private SpecificationRepository specificationRepository;
    private DeviceRepository deviceRepository;
    private RequestRepository requestRepository;
    private WorkRepository workRepository;
    private FileRepository fileRepository;
    private EmployeeRepository employeeRepository;
    private StatusRepository statusRepository;

    public RequestServiceImpl(ClientRepository clientRepository, TypeDeviceRepository typeDeviceRepository,
                              ManufacturerRepository manufacturerRepository, SpecificationRepository specificationRepository,
                              DeviceRepository deviceRepository, RequestRepository requestRepository,
                              WorkRepository workRepository, FileRepository fileRepository,
                              EmployeeRepository employeeRepository, StatusRepository statusRepository) {
        this.clientRepository = clientRepository;
        this.typeDeviceRepository = typeDeviceRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.specificationRepository = specificationRepository;
        this.deviceRepository = deviceRepository;
        this.requestRepository = requestRepository;
        this.workRepository = workRepository;
        this.fileRepository = fileRepository;
        this.employeeRepository = employeeRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public List<Client> getAllClients() throws SQLException {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> getClients(ClientAjaxDto form) throws SQLException {
        return clientRepository.findByFullName(form);
    }

    @Override
    public List<TypeDevice> getAllDevices() throws SQLException {
        return typeDeviceRepository.findAll();
    }

    @Override
    public List<Manufacturer> getManufacturers(Long id) throws SQLException {
        return manufacturerRepository.findByTypeDevice(id);
    }

    @Override
    public List<Specification> getSpecifications(Long manufacturerId, Long typeDeviceId) throws SQLException {
        return specificationRepository.findByTypeDeviceManufacturer(manufacturerId, typeDeviceId);
    }

    @Override
    public Device getDevice(DeviceAjaxDto deviceDto) throws SQLException {
        return deviceRepository.findBySerialNumber(deviceDto.getSerialNumber(), deviceDto.getSpecificationId());
    }

    @Override
    public void saveRequest(CreateRequestDto requestDto) throws SQLException {
        InputStream file = requestDto.getFile();
        FileInfo fileInfo = FileInfo.builder()
                .originalFileName(requestDto.getOriginalFileName())
                .storageFileName(UUID.randomUUID().toString())
                .size(requestDto.getSize())
                .type(requestDto.getContentType())
                .build();
        String storageFileName = fileInfo.getStorageFileName();

        try {
            Files.copy(file, Paths.get("/Users/amirkurmaev/Programming/2024/Java/CRM_Service/files/" + storageFileName + "." + fileInfo.getType().split("/")[1]));
            fileRepository.save(fileInfo);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        UUID uuid = UUID.randomUUID();
        Device device = deviceRepository.findBySerialNumber(requestDto.getSerialNumber(), requestDto.getSpecificationId());

        Request request = new Request();
        request.setDescription(requestDto.getDescription());
        request.setStatus(1L);
        request.setDevice(device.getId());
        request.setClient(requestDto.getClientId());
        request.setIdentifier(uuid);
        request.setFile(fileRepository.findByStorageFileName(storageFileName).getId());

        requestRepository.save(request);
    }

    @Override
    public List<RequestListDto> findAllByStatus(String status) throws SQLException {
        if (status == null || status.isEmpty()) {
            status = "Принято";
        }

        List<RequestListDto> requests = requestRepository.findByStatus(status);
        return requests;
    }

    @Override
    public List<RequestListDto> findAllByIdentifier(String identifier) throws SQLException {
        return requestRepository.findLikeIdentifier(identifier);
    }

    @Override
    public Optional<UUID> validateDetailRequest(String uuid) {
        try {
            UUID identifier = UUID.fromString(uuid);
            return Optional.of(identifier);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<RequestDetailDto> findByIdentifier(UUID identifier) throws SQLException {
        return requestRepository.findByUUID(identifier);
    }

    @Override
    public void updateStatus(UUID identifier, String status) throws SQLException {
        requestRepository.updateStatus(identifier, status);
    }

    @Override
    public List<WorkListDto> findWorksByIdentifier(UUID identifier) throws SQLException {
        return workRepository.findByRequestIdentifier(identifier);
    }

    @Override
    public void deleteBind(String identifierStr, String workIdString) throws SQLException, IllegalArgumentException {
        UUID identifier = UUID.fromString(identifierStr);
        Optional<Long> requestIdOptional = requestRepository.findByIdentifier(identifier);
        if (!requestIdOptional.isPresent()) {
            throw new IllegalArgumentException();
        }
        Long requestId = requestIdOptional.get();
        Long workId = Long.parseLong(workIdString);
        workRepository.deleteBindRequest(requestId, workId);
    }

    @Override
    public List<WorkAddDto> findWorkByModel(String model) throws SQLException {
        return workRepository.findTypesWorkByModel(model);
    }

    @Override
    public void bindWork(String identifierStr, String workIdString, String email) throws SQLException, IllegalArgumentException {
        UUID identifier = UUID.fromString(identifierStr);
        Optional<Long> requestIdOptional = requestRepository.findByIdentifier(identifier);
        if (!requestIdOptional.isPresent()) {
            throw new IllegalArgumentException();
        }
        Long requestId = requestIdOptional.get();
        Long workId = Long.parseLong(workIdString);
        Optional<EmployeeDetailDto> employee = employeeRepository.findByEmail(email);
        requestRepository.bindWork(requestId, workId, employee.get().getId());
    }

    @Override
    public List<Status> getAllStatuses() throws SQLException {
        return statusRepository.findAll();
    }
}
