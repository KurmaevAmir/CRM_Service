package ru.itis.repositories;

import ru.itis.dto.CRM.Manufacturer.ManufacturerAddAjaxDto;
import ru.itis.models.Manufacturer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManufacturerRepositoryJdbcImpl implements ManufacturerRepository {
    private Connection connection;
    private final ManyToManyRelations m2m;

    private static final String SQL_FIND_BY_TYPE_DEVICE = "select m.*\n" +
            "from manufacturer as m\n" +
            "join typedevicemanufacturer as tdm on m.id = tdm.manufacturer\n" +
            "join typedevice as td on tdm.type_device_id = td.id\n" +
            "where td.id = ?";
    private static final String SQL_SELECT_BY_ID = "select * from manufacturer where id = ?";
    private static final String SQL_INSERT = "insert into manufacturer (name) values (?)";
    private static final String SQL_SELECT_BY_NAME = "select id from manufacturer where name = ?";
    private static final String SQL_SELECT_ALL = "select * from manufacturer";
    private static final String SQL_DELETE = "delete from manufacturer where id = ?";

    public ManufacturerRepositoryJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.m2m = new ManyToManyRelations(connection);
    }

    @Override
    public List<Manufacturer> findByTypeDevice(Long id) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_TYPE_DEVICE);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Manufacturer> manufacturers = new ArrayList<>();
        while (resultSet.next()) {
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setId(resultSet.getLong("id"));
            manufacturer.setName(resultSet.getString("name"));
            manufacturers.add(manufacturer);
        }
        return manufacturers;
    }

    @Override
    public Manufacturer findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setId(resultSet.getLong("id"));
            manufacturer.setName(resultSet.getString("name"));
            return manufacturer;
        }
        return null;
    }

    @Override
    public void save(Manufacturer manufacturer) throws SQLException {
        String manufacturerName = manufacturer.getName();

        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        preparedStatement.setString(1, manufacturerName);
        preparedStatement.executeUpdate();

        Long id = findIdByName(manufacturerName);

        m2m.saveRelatedIds("typedevicemanufacturer", "type_device_id", "manufacturer", id, manufacturer.getTypesDevice());
    }

    @Override
    public Long findIdByName(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_NAME);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getLong("id");
        }
        return null;
    }

    @Override
    public List<Manufacturer> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
        List<Manufacturer> manufacturers = new ArrayList<>();
        while (resultSet.next()) {
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setId(resultSet.getLong(1));
            manufacturer.setName(resultSet.getString(2));
            manufacturers.add(manufacturer);
        }
        return manufacturers;
    }

    @Override
    public void delete(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public void binding(Long id, List<Long> typeDeviceId) throws SQLException {
        m2m.saveRelatedIds("typedevicemanufacturer", "type_device_id", "manufacturer", id, typeDeviceId);
    }
}
