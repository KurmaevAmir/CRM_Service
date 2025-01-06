package ru.itis.service;

import ru.itis.dto.CRM.Request.RequestListDto;
import ru.itis.repositories.RequestRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class IndexServiceImpl implements IndexService {
    private final RequestRepository requestRepository;

    public IndexServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public List<RequestListDto> findByClient(Long userId) throws SQLException {
        return requestRepository.findByClient(userId);
    }

    @Override
    public List<RequestListDto> findByEmployee(Long userId) throws SQLException {
        return requestRepository.findByEmployee(userId);
    }
}
