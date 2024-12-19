package ru.itis.repositories;

import ru.itis.models.Specification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecificationRepositoryJdbcImpl implements SpecificationRepository {
    private final Connection connection;

    private static final String SQL_SELECT_BY_ID = "select * from specification where id = ?";
    private static final String SQL_INSERT = "insert into specification (manufacturer, model, article, type_device)" +
            "values (?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "select * from specification";
    private static final String SQL_UPDATE = "update specification set manufacturer = ?, model = ?, article = ?," +
            "type_device = ? where id = ?";
    private static final String SQL_DELETE = "delete from specification where id = ?";

    public SpecificationRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Specification findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createSpecification(resultSet);
        }
        return null;
    }

    @Override
    public void save(Specification entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        preparedStatement.setString(1, entity.getManufacturer());
        preparedStatement.setString(2, entity.getModel());
        preparedStatement.setString(3, entity.getArticle());
        preparedStatement.setLong(4, entity.getTypeDevice());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Specification> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
        List<Specification> specifications = new ArrayList<>();
        while (resultSet.next()) {
            specifications.add(createSpecification(resultSet));
        }
        return specifications;
    }

    @Override
    public void update(Specification entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        preparedStatement.setString(1, entity.getManufacturer());
        preparedStatement.setString(2, entity.getModel());
        preparedStatement.setString(3, entity.getArticle());
        preparedStatement.setLong(4, entity.getTypeDevice());
        preparedStatement.setLong(5, entity.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Specification entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.executeUpdate();
    }

    private Specification createSpecification(ResultSet resultSet) throws SQLException {
        Specification specification = new Specification();
        specification.setId(resultSet.getLong(1));
        specification.setManufacturer(resultSet.getString(2));
        specification.setModel(resultSet.getString(3));
        specification.setArticle(resultSet.getString(4));
        specification.setTypeDevice(resultSet.getLong(5));
        return specification;
    }
}
