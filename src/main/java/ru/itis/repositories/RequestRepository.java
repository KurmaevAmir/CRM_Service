package ru.itis.repositories;

import ru.itis.models.Client;
import ru.itis.models.Request;

import java.sql.SQLException;
import java.util.UUID;

public interface RequestRepository extends CrudRepository<Request> {
    Request findByUUID(UUID uuid) throws SQLException;
    Request findById(Long id) throws SQLException;
    Request findByClient(Client client) throws SQLException;
}
