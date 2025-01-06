package ru.itis.service;

import ru.itis.dto.CreateClientForm;

import java.sql.SQLException;

public interface CreateClientService {
    String createClient(CreateClientForm form) throws SQLException;
}
