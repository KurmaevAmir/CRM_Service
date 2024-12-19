package ru.itis.repositories;

import ru.itis.models.Client;
import ru.itis.models.Request;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RequestRepositoryJdbcImpl implements RequestRepository {
    private final Connection connection;

    private static final String SQL_SELECT_BY_UUID = "select * from request where identifier = ?";
    private static final String SQL_SELECT_BY_ID = "select * from request where id = ?";
    private static final String SQL_SELECT_BY_CLIENT = "select * from request " +
            "where client in (select id from client where (name = ?) or (surname = ?) or (patronymic = ?));";
    private static final String SQL_INSERT = "insert into request (description, date_creation, status, " +
            "device, client, identifier)" +
            "values (?, current_timestamp, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "select * from request";
    private static final String SQL_UPDATE = "update request set description = ?, date_creation = ?, status = ?," +
            "device = ?, client = ?, identifier = ? where id = ?";
    private static final String SQL_DELETE = "delete from request where identifier = ?";

    RequestRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Request findByUUID(UUID uuid) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_UUID);
        preparedStatement.setObject(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createRequest(resultSet);
        }
        return null;
    }

    @Override
    public Request findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createRequest(resultSet);
        }
        return null;
    }

    @Override
    public Request findByClient(Client client) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_CLIENT);
        preparedStatement.setString(1, client.getName());
        preparedStatement.setString(2, client.getSurname());
        preparedStatement.setString(3, client.getPatronymic());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createRequest(resultSet);
        }
        return null;
    }

    @Override
    public void save(Request entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        preparedStatement.setString(1, entity.getDescription());
        preparedStatement.setLong(2, entity.getStatus());
        preparedStatement.setLong(3, entity.getDevice());
        preparedStatement.setLong(4, entity.getClient());
        preparedStatement.setObject(5, entity.getIdentifier());
    }

    @Override
    public List<Request> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
        List<Request> requests = new ArrayList<>();
        while (resultSet.next()) {
            requests.add(createRequest(resultSet));
        }
        return requests;
    }

    @Override
    public void update(Request entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        preparedStatement.setString(1, entity.getDescription());
        preparedStatement.setTimestamp(2, entity.getDate_creation());
        preparedStatement.setLong(3, entity.getStatus());
        preparedStatement.setLong(4, entity.getDevice());
        preparedStatement.setLong(5, entity.getClient());
        preparedStatement.setObject(6, entity.getIdentifier());
        preparedStatement.setLong(7, entity.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Request entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.executeUpdate();
    }

    private Request createRequest(ResultSet resultSet) throws SQLException {
        Request request = new Request();
        request.setId(resultSet.getLong(1));
        request.setDescription(resultSet.getString(2));
        request.setDate_creation(resultSet.getTimestamp(3));
        request.setStatus(resultSet.getLong(4));
        request.setDevice(resultSet.getLong(5));
        request.setClient(resultSet.getLong(6));
        request.setIdentifier(UUID.fromString(resultSet.getString(7)));
        return request;
    }
}
