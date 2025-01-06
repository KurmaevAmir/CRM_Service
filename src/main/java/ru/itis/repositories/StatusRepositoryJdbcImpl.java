package ru.itis.repositories;

import ru.itis.models.Status;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatusRepositoryJdbcImpl implements StatusRepository {
    private final Connection connection;

    private static final String SQL_SELECT_BY_ID = "select * from status where id = ?";
    private static final String SQL_INSERT = "insert into status (state) values (?)";
    private static final String SQL_SELECT_ALL = "select * from status";
    private static final String SQL_UPDATE = "update status set state = ? where id = ?";
    private static final String SQL_DELETE = "delete from status where id = ?";

    public StatusRepositoryJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Status findById(Long id) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createStatus(resultSet);
        }
        return null;
    }

    @Override
    public void save(Status entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        preparedStatement.setString(1, entity.getState());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Status> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
        List<Status> statuses = new ArrayList<>();
        while (resultSet.next()) {
            statuses.add(createStatus(resultSet));
        }
        return statuses;
    }

    @Override
    public void update(Status entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        preparedStatement.setString(1, entity.getState());
        preparedStatement.setLong(2, entity.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }

    private Status createStatus(ResultSet resultSet) throws SQLException {
        Status status = new Status();
        status.setId(resultSet.getLong(1));
        status.setState(resultSet.getString(2));
        return status;
    }
}
