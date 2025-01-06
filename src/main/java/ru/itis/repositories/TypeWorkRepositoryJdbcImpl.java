package ru.itis.repositories;

import ru.itis.models.TypeWork;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeWorkRepositoryJdbcImpl implements TypeWorkRepository {
    private final Connection connection;

    private static final String SQL_SELECT_BY_ID = "select * from typework where id = ?";
    private static final String SQL_SELECT_BY_OPERATION = "select t.id from typework as t where operation = ?";
    private static final String SQL_INSERT = "insert into typework (operation) values (?)";
    private static final String SQL_SELECT_ALL = "select * from typework";
    private static final String SQL_UPDATE = "update typework set operation = ? where id = ?";
    private static final String SQL_DELETE = "delete from typework where id = ?";

    public TypeWorkRepositoryJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<TypeWork> findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            TypeWork typeWork = new TypeWork();
            typeWork.setId(id);
            typeWork.setOperation(resultSet.getString("operation"));
            return Optional.of(typeWork);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Long> findByOperation(String operation) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_OPERATION);
        preparedStatement.setString(1, operation);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(resultSet.getLong("id"));
        }
        return Optional.empty();
    }

    @Override
    public void save(TypeWork entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        preparedStatement.setString(1, entity.getOperation());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<TypeWork> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
        List<TypeWork> typeWorks = new ArrayList<>();
        while (resultSet.next()) {
            TypeWork typeWork = new TypeWork();
            typeWork.setId(resultSet.getLong("id"));
            typeWork.setOperation(resultSet.getString("operation"));
            typeWorks.add(typeWork);
        }
        return typeWorks;
    }

    @Override
    public void update(TypeWork entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        preparedStatement.setString(1, entity.getOperation());
        preparedStatement.setLong(2, entity.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }
}
