package ru.itis.repositories;

import ru.itis.models.Work;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkRepositoryJdbcImpl implements WorkRepository {
    private final Connection connection;
    private final ManyToManyRelations m2m;

    private static final String SQL_SELECT_BY_ID = "select * from work where id = ?";
    private static final String SQL_INSERT = "insert into work (type_work, price, warranty, request)" +
            "values (?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "select * from work";
    private static final String SQL_UPDATE = "update work set type_work = ?, price = ?, warranty = ?, request = ? where id = ?";
    private static final String SQL_DELETE = "delete from work where id = ?";
    private static final String SQL_SELECT_RELATIONSHIPS = "select ? from ? where ? = ?";
    private static final String SQL_INSERT_RELATIONSHIPS = "insert into ? (?, ?) values (?, ?)";
    private static final String SQL_DELETE_RELATIONSHIPS = "delete from ? where ? = ?";
    private static final String SQL_INSERT_RELATIONSHIP = "insert into ? (?, ?) values (?, ?)";
    private static final String SQL_DELETE_RELATIONSHIP = "delete from ? where ? = ?";
    private static final String SQL_UPDATE_PRICE_AND_WARRANTY = "update work set price = ?, warranty = ? where id = ?";

    WorkRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
        m2m = new ManyToManyRelations(connection);
    }

    @Override
    public Work findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createWork(resultSet);
        }
        return null;
    }

    @Override
    public void addSparePart(Long workId, Long sparePartId) throws SQLException {
        saveSingleRelation("sparepartwork", "sparepart_id", "work_id", sparePartId, workId);
    }

    @Override
    public void addEmployee(Long workId, Long employeeId) throws SQLException {
        saveSingleRelation("workemployee", "employee_id", "work_id", employeeId, workId);
    }

    @Override
    public void changePriceAndWarranty(Long workId, Integer newPrice, Integer newWarranty) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_PRICE_AND_WARRANTY);
        preparedStatement.setLong(1, newPrice);
        preparedStatement.setInt(2, newWarranty);
        preparedStatement.setLong(3, workId);
        preparedStatement.executeUpdate();
    }

    @Override
    public void save(Work entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        prepareStatement(preparedStatement, entity);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            entity.setId(resultSet.getLong("id"));
        }
        m2m.saveRelatedIds("sparepartwork", "sparepart_id", "work_id", entity.getId(), entity.getSparePart_id());
        m2m.saveRelatedIds("workemployee", "employee_id", "work_id", entity.getId(), entity.getEmployee_id());
    }

    @Override
    public List<Work> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
        List<Work> works = new ArrayList<>();
        while (resultSet.next()) {
            works.add(createWork(resultSet));
        }
        return works;
    }

    @Override
    public void update(Work entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        prepareStatement(preparedStatement, entity);
        preparedStatement.setLong(5, entity.getId());
        preparedStatement.executeUpdate();
        m2m.deleteRelatedIds("sparepartwork", "work_id", entity.getId());
        m2m.deleteRelatedIds("workemployee", "work_id", entity.getId());
        m2m.saveRelatedIds("sparepartwork", "sparepart_id", "work_id", entity.getId(), entity.getSparePart_id());
        m2m.saveRelatedIds("workemployee", "employee_id", "work_id", entity.getId(), entity.getEmployee_id());
    }

    @Override
    public void delete(Work entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.executeUpdate();
    }

    private Work createWork(ResultSet resultSet) throws SQLException {
        Work work = new Work();
        work.setId(resultSet.getLong(1));
        work.setTypeWork(resultSet.getLong(2));
        work.setPrice((resultSet.getInt(3)));
        work.setWarranty((resultSet.getInt(4)));
        work.setRequest(resultSet.getLong(5));
        work.setSparePart_id(m2m.getRelatedIds("sparepartwork", "sparepart_id", "work_id", work.getId()));
        work.setEmployee_id(m2m.getRelatedIds("workemployee", "employee_id", "work_id", work.getId()));
        return work;
    }

    private void prepareStatement(PreparedStatement preparedStatement, Work entity) throws SQLException {
        preparedStatement.setLong(1, entity.getTypeWork());
        preparedStatement.setInt(2, entity.getPrice());
        preparedStatement.setInt(3, entity.getWarranty());
        preparedStatement.setLong(4, entity.getRequest());
    }

    private void saveSingleRelation(String table, String targetColumn, String sourceColumn, Long targetId, Long sourceId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_RELATIONSHIP);
        preparedStatement.setString(1, table);
        preparedStatement.setString(2, targetColumn);
        preparedStatement.setString(3, sourceColumn);
        preparedStatement.setLong(4, targetId);
        preparedStatement.setLong(5, sourceId);
        preparedStatement.executeUpdate();
    }

    private void deleteSingleRelation(String table, String sourceColumn, Long sourceId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_RELATIONSHIP);
        preparedStatement.setString(1, table);
        preparedStatement.setString(2, sourceColumn);
        preparedStatement.setLong(3, sourceId);
        preparedStatement.executeUpdate();
    }
}
