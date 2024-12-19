package ru.itis.repositories;

import ru.itis.models.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepositoryJdbcImpl implements ClientRepository {
    private final Connection connection;

    private static final String SQL_SELECT_BY_ID = "select * from client where id = ?";
    private static final String SQL_SELECT_ALL_EMAIL = "select email from client";
    private static final String SQL_SELECT_BY_EMAIL = "select * from client where email = ?";
    private static final String SQL_INSERT = "insert into client (name, surname, patronymic, " +
            "date_of_birth, phone_number, email, passport, password)" +
            "values (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "select * from client";
    private static final String SQL_UPDATE = "update client set name = ?, surname = ?, " +
            "patronymic = ?, date_of_birth = ?, phone_number = ?, email = ?, passport = ?, " +
            "password = ? where id = ?";
    private static final String SQL_DELETE = "delete from client where id = ?";

    ClientRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Client findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createClient(resultSet);
        }
        return null;
    }

    @Override
    public List<String> findAllEmail() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_EMAIL);
        List<String> emails = new ArrayList<>();
        while (resultSet.next()) {
            emails.add(resultSet.getString(1));
        }
        return emails;
    }

    @Override
    public List<Client> findByEmail() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_EMAIL);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Client> clients = new ArrayList<>();
        while (resultSet.next()) {
            clients.add(createClient(resultSet));
        }
        return clients;
    }

    @Override
    public void save(Client entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        prepareStatement(preparedStatement, entity);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Client> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
        List<Client> clients = new ArrayList<>();
        while (resultSet.next()) {
            clients.add(createClient(resultSet));
        }
        return clients;
    }

    @Override
    public void update(Client entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        prepareStatement(preparedStatement, entity);
        preparedStatement.setLong(9, entity.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Client entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.executeUpdate();
    }

    private Client createClient(ResultSet resultSet) throws SQLException {
        Client client = new Client();
        client.setId(resultSet.getLong(1));
        client.setName(resultSet.getString(2));
        client.setSurname(resultSet.getString(3));
        client.setPatronymic(resultSet.getString(4));
        client.setDate_of_birth(resultSet.getDate(5));
        client.setPhone_number(resultSet.getString(6));
        client.setEmail(resultSet.getString(7));
        client.setPassport(resultSet.getLong(8));
        client.setPassword(resultSet.getString(9));
        return client;
    }

    private void prepareStatement(PreparedStatement preparedStatement, Client entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getSurname());
        preparedStatement.setString(3, entity.getPatronymic());
        preparedStatement.setDate(4, entity.getDate_of_birth());
        preparedStatement.setString(5, entity.getPhone_number());
        preparedStatement.setString(6, entity.getEmail());
        preparedStatement.setLong(7, entity.getPassport());
        preparedStatement.setString(8, entity.getPassword());
    }
}
