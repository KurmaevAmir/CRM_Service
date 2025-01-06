package ru.itis.repositories;

import ru.itis.models.Device;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceRepositoryJdbcImpl implements DeviceRepository {
    private final Connection connection;

    private static final String SQL_SELECT_BY_ID = "select * from device where id = ?";
    private static final String SQL_INSET = "insert into device (serial_number, color, specification) " +
            "values (?, ?, ?)";
    private static final String SQL_SELECT_ALL = "select * from device";
    private static final String SQL_UPDATE = "update device set serial_number = ?, color = ?, specification = ? " +
            "where id = ?";
    private static final String SQL_DELETE = "delete from device where id = ?";
    private static final String SQL_SELECT_BY_SERIAL_NUMBER = "select * from device where serial_number = ? and specification = ?";


    public DeviceRepositoryJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Device findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createDevice(resultSet);
        }
        return null;
    }

    @Override
    public void save(Device entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSET);
        preparedStatement.setString(1, entity.getSerialNumber());
        preparedStatement.setString(2, entity.getColor());
        preparedStatement.setLong(3, entity.getSpecification());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Device> findAll() throws SQLException {
        List<Device> devices = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            devices.add(createDevice(resultSet));
        }
        return devices;
    }

    @Override
    public void update(Device entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        preparedStatement.setString(1, entity.getSerialNumber());
        preparedStatement.setString(2, entity.getColor());
        preparedStatement.setLong(3, entity.getSpecification());
        preparedStatement.setLong(4, entity.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public Device findBySerialNumber(String serialNumber, Long specification) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_SERIAL_NUMBER);
        preparedStatement.setString(1, serialNumber);
        preparedStatement.setLong(2,  specification);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createDevice(resultSet);
        }
        return null;
    }

    private Device createDevice(ResultSet resultSet) throws SQLException {
        Device device = new Device();
        device.setId(resultSet.getLong(1));
        device.setSerialNumber(resultSet.getString(2));
        device.setColor(resultSet.getString(3));
        device.setSpecification(resultSet.getLong(4));
        return device;
    }
}
