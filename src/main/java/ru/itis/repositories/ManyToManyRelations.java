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

    private static final String SQL_SELECT_RELATIONSHIPS = "select %s from %s where %s = ?";
    private static final String SQL_INSERT_RELATIONSHIPS = "insert into %s (%s, %s) values (?, ?)";
    private static final String SQL_DELETE_RELATIONSHIPS = "delete from %s where %s = ?";
    private static final String SQL_DELETE_ALL_RELATIONSHIPS = "delete from %s where %s = ? and %s = ?";

    public List<Long> getRelatedIds(String table, String targetColumn, String sourceColumn, Long sourceId) throws SQLException {
        List<Long> ids = new ArrayList<>();
        String CUSTOM_SQL_SELECT_RELATIONSHIPS = String.format(SQL_SELECT_RELATIONSHIPS, targetColumn, table, sourceColumn);
        PreparedStatement preparedStatement = connection.prepareStatement(CUSTOM_SQL_SELECT_RELATIONSHIPS);
        preparedStatement.setLong(1, sourceId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            ids.add(resultSet.getLong(targetColumn));
        }
        return ids;
    }

    public void saveRelatedIds(String table, String targetColumn, String sourceColumn, Long sourceId, List<Long> targetIds) throws SQLException {
        String CUSTOM_SQL_INSERT_RELATIONSHIPS = String.format(SQL_INSERT_RELATIONSHIPS, table, targetColumn, sourceColumn);
        PreparedStatement preparedStatement = connection.prepareStatement(CUSTOM_SQL_INSERT_RELATIONSHIPS);
        for (Long targetId : targetIds) {
            preparedStatement.setLong(1, targetId);
            preparedStatement.setLong(2, sourceId);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    public void deleteRelatedIds(String table, String sourceColumn, Long sourceId) throws SQLException {
        String CUSTOM_SQL_DELETE_RELATIONSHIPS = String.format(SQL_DELETE_RELATIONSHIPS, table, sourceColumn);
        PreparedStatement preparedStatement = connection.prepareStatement(CUSTOM_SQL_DELETE_RELATIONSHIPS);
        preparedStatement.setLong(1, sourceId);
        preparedStatement.executeUpdate();
    }

    public void deleteAllRelations(String table, String targetColumn, String sourceColumn, Long sourceId, Long targetIds) throws SQLException {
        String CUSTOM_SQL_DELETE_RELATIONSHIPS = String.format(SQL_DELETE_ALL_RELATIONSHIPS, table, targetColumn, sourceColumn);
        PreparedStatement preparedStatement = connection.prepareStatement(CUSTOM_SQL_DELETE_RELATIONSHIPS);
        preparedStatement.setLong(1, targetIds);
        preparedStatement.setLong(2, sourceId);
        preparedStatement.executeUpdate();
    }
}
