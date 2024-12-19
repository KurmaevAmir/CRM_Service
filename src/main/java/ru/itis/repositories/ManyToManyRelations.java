package ru.itis.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManyToManyRelations {
    private final Connection connection;

    ManyToManyRelations(Connection connection) {
        this.connection = connection;
    }

    private static final String SQL_SELECT_RELATIONSHIPS = "select ? from ? where ? = ?";
    private static final String SQL_INSERT_RELATIONSHIPS = "insert into ? (?, ?) values (?, ?)";
    private static final String SQL_DELETE_RELATIONSHIPS = "delete from ? where ? = ?";
    private static final String SQL_INSERT_RELATIONSHIP = "insert into ? (?, ?) values (?, ?)";
    private static final String SQL_DELETE_RELATIONSHIP = "delete from ? where ? = ?";

    public List<Long> getRelatedIds(String table, String targetColumn, String sourceColumn, Long sourceId) throws SQLException {
        List<Long> ids = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_RELATIONSHIPS);
        preparedStatement.setString(1, targetColumn);
        preparedStatement.setString(2, table);
        preparedStatement.setString(3, sourceColumn);
        preparedStatement.setLong(4, sourceId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            ids.add(resultSet.getLong(targetColumn));
        }
        return ids;
    }

    public void saveRelatedIds(String table, String targetColumn, String sourceColumn, Long sourceId, List<Long> targetIds) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_RELATIONSHIPS);
        preparedStatement.setString(1, table);
        preparedStatement.setString(2, targetColumn);
        preparedStatement.setString(3, sourceColumn);
        for (Long targetId : targetIds) {
            preparedStatement.setLong(4, targetId);
            preparedStatement.setLong(5, sourceId);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    public void deleteRelatedIds(String table, String sourceColumn, Long sourceId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_RELATIONSHIPS);
        preparedStatement.setString(1, table);
        preparedStatement.setString(2, sourceColumn);
        preparedStatement.setLong(3, sourceId);
        preparedStatement.executeUpdate();
    }
}
