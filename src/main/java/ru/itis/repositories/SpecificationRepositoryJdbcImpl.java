package ru.itis.repositories;

import ru.itis.dto.CRM.Specification.SpecificationListDto;
import ru.itis.models.Specification;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecificationRepositoryJdbcImpl implements SpecificationRepository {
    private final Connection connection;

    private static final String SQL_SELECT_BY_ID = "select * from specification where id = ?";
    private static final String SQL_INSERT = "insert into specification (type_device, manufacturer, model, article)" +
            "values (?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "select * from specification";
    private static final String SQL_UPDATE = "update specification set type_device = ?, model = ?, article = ?, " +
            "manufacturer = ? where id = ?";
    private static final String SQL_DELETE = "delete from specification where id = ?";
    private static final String SQL_SELECT_BY_TYPE_DEVICE_MANUFACTURER = "select * from specification where type_device = ? and manufacturer = ?";
    private static final String SQL_SELECT_BY_MANUFACTURER = "select s.id, m.name, s.model, s.article, td.name " +
            "from specification as s join manufacturer as m on s.manufacturer = m.id " +
            "join typedevice as td on s.type_device = td.id where m.id = ? and td.id = ?";
    private static final String SQL_SELECT_ALL_BY_DTO = "select s.id, m.name, s.model, s.article, td.name from specification as s " +
            "join manufacturer as m on s.manufacturer = m.id join typedevice as td on s.type_device = td.id";

    public SpecificationRepositoryJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Specification> findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(createSpecification(resultSet));
        }
        return Optional.empty();
    }

    @Override
    public void save(Specification entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        preparedStatement.setLong(1, entity.getTypeDevice());
        preparedStatement.setLong(2, entity.getManufacturer());
        preparedStatement.setString(3, entity.getModel());
        preparedStatement.setString(4, entity.getArticle());
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
        preparedStatement.setLong(1, entity.getTypeDevice());
        preparedStatement.setString(2, entity.getModel());
        preparedStatement.setString(3, entity.getArticle());
        preparedStatement.setLong(4, entity.getManufacturer());
        preparedStatement.setLong(5, entity.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Specification> findByTypeDeviceManufacturer(Long manufacturerId, Long TypeDeviceId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_TYPE_DEVICE_MANUFACTURER);
        preparedStatement.setLong(1, TypeDeviceId);
        preparedStatement.setLong(2, manufacturerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Specification> specifications = new ArrayList<>();
        while (resultSet.next()) {
            specifications.add(createSpecification(resultSet));
        }
        return specifications;
    }

    @Override
    public List<SpecificationListDto> findByManufacturerAndTypeDevice(Long manufacturerId, Long typeDeviceId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_MANUFACTURER);
        preparedStatement.setLong(1, manufacturerId);
        preparedStatement.setLong(2, typeDeviceId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<SpecificationListDto> specifications = new ArrayList<>();
        while (resultSet.next()) {
            SpecificationListDto dto = SpecificationListDto.builder()
                    .id(resultSet.getLong(1))
                    .manufacturer(resultSet.getString(2))
                    .model(resultSet.getString(3))
                    .article(resultSet.getString(4))
                    .typeDevice(resultSet.getString(5))
                    .build();
            specifications.add(dto);
        }
        return specifications;
    }

    @Override
    public List<SpecificationListDto> findAllByDto() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_BY_DTO);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<SpecificationListDto> specifications = new ArrayList<>();
        while (resultSet.next()) {
            SpecificationListDto dto = SpecificationListDto.builder()
                    .id(resultSet.getLong(1))
                    .manufacturer(resultSet.getString(2))
                    .model(resultSet.getString(3))
                    .article(resultSet.getString(4))
                    .typeDevice(resultSet.getString(5))
                    .build();
            specifications.add(dto);
        }
        return specifications;
    }

    private Specification createSpecification(ResultSet resultSet) throws SQLException {
        Specification specification = new Specification();
        specification.setId(resultSet.getLong(1));
        specification.setTypeDevice(resultSet.getLong(4));
        specification.setManufacturer(resultSet.getLong(5));
        specification.setModel(resultSet.getString(2));
        specification.setArticle(resultSet.getString(3));
        return specification;
    }
}
