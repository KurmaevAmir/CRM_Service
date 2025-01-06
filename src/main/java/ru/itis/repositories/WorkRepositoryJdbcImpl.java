package ru.itis.repositories;

import ru.itis.dto.CRM.Work.WorkAddDto;
import ru.itis.dto.CRM.Work.WorkListDto;
import ru.itis.models.Work;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkRepositoryJdbcImpl implements WorkRepository {
    private final Connection connection;
    private final ManyToManyRelations m2m;

    private static final String SQL_SELECT_BY_ID = "select * from work where id = ?";
    private static final String SQL_INSERT = "insert into work (type_work, price, warranty, specification)" +
            "values (?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "select * from work";
    private static final String SQL_UPDATE = "update work set type_work = ?, price = ?, warranty = ?, specification = ? where id = ?";
    private static final String SQL_DELETE = "delete from work where id = ?";
    private static final String SQL_INSERT_RELATIONSHIP = "insert into ? (?, ?) values (?, ?)";
    private static final String SQL_DELETE_RELATIONSHIP = "delete from ? where ? = ?";
    private static final String SQL_UPDATE_PRICE_AND_WARRANTY = "update work set price = ?, warranty = ? where id = ?";
    private static final String SQL_SELECT_BY_SPECIFICATION = "select tw.operation, w.price, w.warranty, m.name, " +
            "s.model, s.article, w.id from work as w join typework as tw on w.type_work = tw.id " +
            "join specification as s on w.specification = s.id join manufacturer as m on s.manufacturer = m.id " +
            "where s.id = ?";
    private static final String SQL_SELECT_ALL_FOR_WORK_LIST_DTO = "select tw.operation, w.price, w.warranty, m.name, " +
            "s.model, s.article, w.id from work as w join typework as tw on w.type_work = tw.id " +
            "join specification as s on w.specification = s.id join manufacturer as m on s.manufacturer = m.id";
    private static final String SQL_SELECT_BY_REQUEST_IDENTIFIER = "select tw.operation, w.price, w.warranty, m.name, " +
            "s.model, s.article, w.id from work as w join typework as tw on w.type_work = tw.id " +
            "join specification as s on w.specification = s.id join manufacturer as m on s.manufacturer = m.id " +
            "join workrequest wr on w.id = wr.work_id join request r on wr.request_id = r.id where r.identifier = ?";
    private static final String SQL_SELECT_TYPE_WORK_BY_MODEL = "select w.id, tw.operation from work as w " +
            "join specification as s on w.specification = s.id join typework as tw on w.type_work = tw.id " +
            "where s.model = ?";

    public WorkRepositoryJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        preparedStatement.executeUpdate();
//        m2m.saveRelatedIds("sparepartwork", "sparepart_id", "work_id", entity.getId(), entity.getSparePart_id());
//        m2m.saveRelatedIds("workemployee", "employee_id", "work_id", entity.getId(), entity.getEmployee_id());
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
        preparedStatement.setLong(4, entity.getId());
        preparedStatement.executeUpdate();
        m2m.deleteRelatedIds("sparepartwork", "work_id", entity.getId());
        m2m.deleteRelatedIds("workemployee", "work_id", entity.getId());
        m2m.saveRelatedIds("workemployee", "employee_id", "work_id", entity.getId(), entity.getEmployee_id());
    }

    @Override
    public void delete(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
        m2m.deleteRelatedIds("workrequest", "work_id", id);
        m2m.deleteRelatedIds("workemployee", "work_id", id);
    }

    private Work createWork(ResultSet resultSet) throws SQLException {
        Work work = new Work();
        work.setId(resultSet.getLong(1));
        work.setTypeWork(resultSet.getLong(2));
        work.setPrice((resultSet.getDouble(3)));
        work.setWarranty((resultSet.getInt(4)));
        work.setSpecification(resultSet.getLong(5));
//        work.setEmployee_id(m2m.getRelatedIds("workemployee", "employee_id", "work_id", work.getId()));
        return work;
    }

    @Override
    public List<WorkListDto> findBySpecification(Long specificationId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_SPECIFICATION);
        preparedStatement.setLong(1, specificationId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<WorkListDto> workListDtos = new ArrayList<>();
        while (resultSet.next()) {
            WorkListDto dto = WorkListDto.builder()
                    .typeWork(resultSet.getString(1))
                    .price(resultSet.getDouble(2))
                    .warranty(resultSet.getInt(3))
                    .manufacturer(resultSet.getString(4))
                    .model(resultSet.getString(5))
                    .article(resultSet.getString(6))
                    .id(resultSet.getLong(7))
                    .build();
            workListDtos.add(dto);
        }
        return workListDtos;
    }

    @Override
    public List<WorkListDto> findAllWorkListDto() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_FOR_WORK_LIST_DTO);
        List<WorkListDto> workListDtos = new ArrayList<>();
        while (resultSet.next()) {
            WorkListDto dto = WorkListDto.builder()
                    .typeWork(resultSet.getString(1))
                    .price(resultSet.getDouble(2))
                    .warranty(resultSet.getInt(3))
                    .manufacturer(resultSet.getString(4))
                    .model(resultSet.getString(5))
                    .article(resultSet.getString(6))
                    .id(resultSet.getLong(7))
                    .build();
            workListDtos.add(dto);
        }
        return workListDtos;
    }

    @Override
    public List<WorkListDto> findByRequestIdentifier(UUID identifier) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_REQUEST_IDENTIFIER);
        preparedStatement.setObject(1, identifier);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<WorkListDto> workListDtos = new ArrayList<>();
        while (resultSet.next()) {
            WorkListDto dto = WorkListDto.builder()
                    .typeWork(resultSet.getString(1))
                    .price(resultSet.getDouble(2))
                    .warranty(resultSet.getInt(3))
                    .manufacturer(resultSet.getString(4))
                    .model(resultSet.getString(5))
                    .article(resultSet.getString(6))
                    .id(resultSet.getLong(7))
                    .build();
            workListDtos.add(dto);
        }
        return workListDtos;
    }

    @Override
    public void deleteBindRequest(Long request, Long workId) throws SQLException {
        m2m.deleteAllRelations("workrequest", "work_id", "request_id", request, workId);
    }

    @Override
    public List<WorkAddDto> findTypesWorkByModel(String model) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_TYPE_WORK_BY_MODEL);
        preparedStatement.setString(1, model);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<WorkAddDto> types = new ArrayList<>();
        while (resultSet.next()) {
            WorkAddDto dto = WorkAddDto.builder()
                    .id(resultSet.getLong(1))
                    .typeWork(resultSet.getString(2))
                    .build();
            types.add(dto);
        }
        return types;
    }

    private void prepareStatement(PreparedStatement preparedStatement, Work entity) throws SQLException {
        preparedStatement.setLong(1, entity.getTypeWork());
        preparedStatement.setDouble(2, entity.getPrice());
        preparedStatement.setInt(3, entity.getWarranty());
        preparedStatement.setLong(4, entity.getSpecification());
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
