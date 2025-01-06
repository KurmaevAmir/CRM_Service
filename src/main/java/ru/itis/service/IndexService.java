package ru.itis.service;

import ru.itis.dto.CRM.Request.RequestListDto;
import ru.itis.dto.RequestDto;

import java.sql.SQLException;
import java.util.List;

public interface IndexService {
    List<RequestListDto> findByClient(Long userId) throws SQLException;
    List<RequestListDto> findByEmployee(Long userId) throws SQLException;
}
