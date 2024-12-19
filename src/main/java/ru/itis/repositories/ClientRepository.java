package ru.itis.repositories;

import ru.itis.models.Client;

import java.sql.SQLException;
import java.util.List;

public interface ClientRepository extends CrudRepository<Client> {
    Client findById(Long id) throws SQLException;
    List<String> findAllEmail() throws SQLException;
    List<Client> findByEmail() throws SQLException;
}
