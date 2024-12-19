package ru.itis.repositories;

import ru.itis.models.TypeDevice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeDeviceRepositoryJdbcImpl implements TypeDeviceRepository {
    private final Connection connection;

    private static final String SQL_SELECT_BY_ID = "select * from typeDevice where id = ?";
    private static final String SQL_INSERT = "insert into typeDevice (name) values (?)";
    private static final String SQL_SELECT_ALL = "select * from typeDevice";
    private static final String SQL_UPDATE = "update typeDevice set name = ? where id = ?";
    private static final String SQL_DELETE = "delete from typeDevice where id = ?";

    TypeDeviceRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public TypeDevice getTypeDeviceById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            TypeDevice typeDevice = new TypeDevice();
            typeDevice.setId(resultSet.getLong("id"));
            typeDevice.setName(resultSet.getString("name"));
            return typeDevice;
        }
        return null;
    }

    @Override
    public void save(TypeDevice entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        preparedStatement.setString(1, entity.getName());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<TypeDevice> findAll() throws SQLException {
        List<TypeDevice> typeDevices = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
        while (resultSet.next()) {
            TypeDevice typeDevice = new TypeDevice();
            typeDevice.setId(resultSet.getLong("id"));
            typeDevice.setName(resultSet.getString("name"));
            typeDevices.add(typeDevice);
        }
        return typeDevices;
    }

    @Override
    public void update(TypeDevice entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setLong(2, entity.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(TypeDevice entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.executeUpdate();
    }
}
