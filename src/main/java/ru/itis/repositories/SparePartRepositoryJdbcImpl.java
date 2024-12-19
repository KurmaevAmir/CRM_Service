package ru.itis.repositories;

import ru.itis.models.SparePart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SparePartRepositoryJdbcImpl implements SparePartRepository {
    private final Connection connection;
    private final ManyToManyRelations m2m;

    private static final String SQL_SELECT_BY_ARTICLE = "select * from sparepart where article = ?";
    private static final String SQL_SELECT_BY_NAME = "select * from sparepart where name = ?";
    private static final String SQL_SELECT_BY_ID = "select * from sparepart where id = ?";
    private static final String SQL_INSERT = "insert into sparepart (name, number, article)" +
            "values (?, ?, ?)";
    private static final String SQL_FIND_ALL = "select * from sparepart";
    private static final String SQL_UPDATE = "update sparepart set name = ?, number = ?, article = ? where id = ?";
    private static final String SQL_DELETE = "delete from sparepart where id = ?";

    SparePartRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
        this.m2m = new ManyToManyRelations(connection);
    }

    @Override
    public SparePart findByArticle(String article) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ARTICLE);
        preparedStatement.setString(1, article);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createSparePart(resultSet);
        }
        return null;
    }

    @Override
    public SparePart findByName(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_NAME);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createSparePart(resultSet);
        }
        return null;
    }

    @Override
    public SparePart findById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createSparePart(resultSet);
        }
        return null;
    }

    @Override
    public void save(SparePart entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        prepareStatement(preparedStatement, entity);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            entity.setId(resultSet.getLong("id"));
        }
        m2m.saveRelatedIds("sparepartwork", "work_id", "sparepart_id", entity.getId(), entity.getWork());
    }

    @Override
    public List<SparePart> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL);
        List<SparePart> spareParts = new ArrayList<>();
        while (resultSet.next()) {
            spareParts.add(createSparePart(resultSet));
        }
        return spareParts;
    }

    @Override
    public void update(SparePart entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        prepareStatement(preparedStatement, entity);
        preparedStatement.setLong(4, entity.getId());
        preparedStatement.executeUpdate();
        m2m.deleteRelatedIds("sparepartwork", "sparepart_id", entity.getId());
        m2m.saveRelatedIds("sparepartwork", "work_id", "sparepart_id", entity.getId(), entity.getWork());

    }

    @Override
    public void delete(SparePart entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.executeUpdate();
    }

    private SparePart createSparePart(ResultSet resultSet) throws SQLException {
        SparePart sparePart = new SparePart();
        sparePart.setName(resultSet.getString("name"));
        sparePart.setNumber(resultSet.getInt("number"));
        sparePart.setArticle(resultSet.getString("article"));
        sparePart.setWork(m2m.getRelatedIds("sparepartwork", "work_id", "sparepart_id", sparePart.getId()));
        return sparePart;
    }

    private void prepareStatement(PreparedStatement preparedStatement, SparePart entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setInt(2, entity.getNumber());
        preparedStatement.setString(3, entity.getArticle());
    }
}
