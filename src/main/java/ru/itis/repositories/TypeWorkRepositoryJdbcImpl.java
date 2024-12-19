package ru.itis.repositories;

import ru.itis.models.TypeWork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeWorkRepositoryJdbcImpl implements TypeWorkRepository {
    private final Connection connection;

    private static final String SQL_SELECT_BY_ID = "select * from typework where id = ?";
    private static final String SQL_INSERT = "insert into typework (operation) values (?)";
    private static final String SQL_SELECT_ALL = "select * from typework";
    private static final String SQL_UPDATE = "update typework set operation = ? where id = ?";
    private static final String SQL_DELETE = "delete from typework where id = ?";

    TypeWorkRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public TypeWork findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            TypeWork typeWork = new TypeWork();
            typeWork.setId(id);
            typeWork.setOperation(resultSet.getString("operation"));
            return typeWork;
        }
        return null;
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
    public void delete(TypeWork entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.executeUpdate();
    }
}
