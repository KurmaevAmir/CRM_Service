package ru.itis.repositories;

import ru.itis.dto.CRM.Request.RequestDetailDto;
import ru.itis.dto.CRM.Request.RequestListDto;
import ru.itis.models.Client;
import ru.itis.models.Request;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RequestRepositoryJdbcImpl implements RequestRepository {
    private final Connection connection;
    private final ManyToManyRelations m2m;

    private static final String SQL_SELECT_BY_UUID = "select r.identifier, r.description, r.date_creation, s.state, " +
            "m.name, sp.model, sp.article, d.serial_number, d.color, c.name, c.surname, c.patronymic, c.phone_number, " +
            "c.email, r.file from request as r join device as d on d.id = r.device " +
            "join specification as sp on sp.id = d.specification join manufacturer as m on m.id = sp.manufacturer " +
            "join status as s on s.id = r.status join client as c on c.id = r.client where r.identifier = ?";
    private static final String SQL_SELECT_BY_ID = "select * from request where id = ?";
    private static final String SQL_SELECT_BY_CLIENT = "select * from request " +
            "where client in (select id from client where (name = ?) or (surname = ?) or (patronymic = ?));";
    private static final String SQL_INSERT = "insert into request (description, date_creation, status, " +
            "device, client, identifier, file)" +
            "values (?, current_timestamp, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "select * from request";
    private static final String SQL_UPDATE = "update request set description = ?, date_creation = ?, status = ?," +
            "device = ?, client = ?, identifier = ? where id = ?";
    private static final String SQL_DELETE = "delete from request where identifier = ?";
    private static final String SQL_SELECT_BY_STATUS = "select r.identifier, m.name, s.model, d.serial_number, " +
            "c.surname, st.state, r.date_creation from request as r join device as d on r.device = d.id " +
            "join specification as s on d.specification = s.id join manufacturer as m on s.manufacturer = m.id " +
            "join status as st on st.id = r.status join client as c on c.id = r.client where st.state = ?";
    private static final String SQL_UPDATE_STATUS = "UPDATE request SET status = (select id from status where state = ?) WHERE identifier = ?";
    private static final String SQL_SELECT_LIKE_IDENTIFIER = "select r.identifier, m.name, s.model, d.serial_number, c.surname, " +
            "st.state, r.date_creation as ident from request as r join device as d on r.device = d.id " +
            "join specification as s on d.specification = s.id join manufacturer as m on s.manufacturer = m.id " +
            "join status as st on st.id = r.status join client as c on c.id = r.client " +
            "where r.identifier::varchar like ?";
    private static final String SQL_SELECT_BY_IDENTIFIER = "select r.id from request as r where r.identifier = ?";
    private static final String SQL_SELECT_BY_CLIENT_ID = "select r.identifier, m.name, s.model, d.serial_number, " +
            "c.surname, st.state, r.date_creation from request as r join device as d on r.device = d.id " +
            "join specification as s on d.specification = s.id join manufacturer as m on s.manufacturer = m.id " +
            "join status as st on st.id = r.status join client as c on c.id = r.client where c.id = ?";
    private static final String SQL_SELECT_BY_EMPLOYEE = "select r.identifier, m.name, s.model, d.serial_number, " +
            "st.state, r.date_creation from request as r join device as d on r.device = d.id " +
            "join specification as s on d.specification = s.id join manufacturer as m on s.manufacturer = m.id " +
            "join status as st on st.id = r.status join requestemployee as re on r.id = re.request_id " +
            "join employee as e on re.employee_id = e.id where e.id = ?";

    public RequestRepositoryJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        m2m = new ManyToManyRelations(connection);
    }

    @Override
    public Optional<RequestDetailDto> findByUUID(UUID uuid) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_UUID);
        preparedStatement.setObject(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            RequestDetailDto request = new RequestDetailDto();
            request.setIdentifier(resultSet.getString(1));
            request.setDescription(resultSet.getString(2));
            request.setDate(resultSet.getTimestamp(3));
            request.setStatus(resultSet.getString(4));
            request.setManufacturer(resultSet.getString(5));
            request.setModel(resultSet.getString(6));
            request.setArticle(resultSet.getString(7));
            request.setSerialNumber(resultSet.getString(8));
            request.setColor(resultSet.getString(9));
            request.setClientName(resultSet.getString(10));
            request.setClientSurname(resultSet.getString(11));
            request.setClientPatronymic(resultSet.getString(12));
            request.setClientPhone(resultSet.getString(13));
            request.setClientEmail(resultSet.getString(14));
            request.setFile(resultSet.getLong(15));
            return Optional.of(request);
        }
        return Optional.empty();
    }

    @Override
    public Request findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createRequest(resultSet);
        }
        return null;
    }

    @Override
    public Request findByClient(Client client) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_CLIENT);
        preparedStatement.setString(1, client.getName());
        preparedStatement.setString(2, client.getSurname());
        preparedStatement.setString(3, client.getPatronymic());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return createRequest(resultSet);
        }
        return null;
    }

    @Override
    public void save(Request entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        preparedStatement.setString(1, entity.getDescription());
        preparedStatement.setLong(2, entity.getStatus());
        preparedStatement.setLong(3, entity.getDevice());
        preparedStatement.setLong(4, entity.getClient());
        preparedStatement.setObject(5, entity.getIdentifier());
        preparedStatement.setLong(6, entity.getFile());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Request> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
        List<Request> requests = new ArrayList<>();
        while (resultSet.next()) {
            requests.add(createRequest(resultSet));
        }
        return requests;
    }

    @Override
    public void update(Request entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        preparedStatement.setString(1, entity.getDescription());
        preparedStatement.setTimestamp(2, entity.getDate_creation());
        preparedStatement.setLong(3, entity.getStatus());
        preparedStatement.setLong(4, entity.getDevice());
        preparedStatement.setLong(5, entity.getClient());
        preparedStatement.setObject(6, entity.getIdentifier());
        preparedStatement.setLong(7, entity.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<RequestListDto> findByStatus(String status) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_STATUS);
        preparedStatement.setString(1, status);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<RequestListDto> requests = new ArrayList<>();
        while (resultSet.next()) {
            RequestListDto request = new RequestListDto();
            request.setIdentifier(UUID.fromString(resultSet.getString(1)));
            request.setManufacturer(resultSet.getString(2));
            request.setModel(resultSet.getString(3));
            request.setSerialNumber(resultSet.getString(4));
            request.setStatus(resultSet.getString(5));
            request.setClientSurname(resultSet.getString(6));
            request.setDate_creation(resultSet.getTimestamp(7));
            requests.add(request);
        }
        return requests;
    }

    @Override
    public void updateStatus(UUID identifier, String status) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_STATUS);
        preparedStatement.setString(1, status);
        preparedStatement.setObject(2, identifier);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<RequestListDto> findLikeIdentifier(String identifier) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_LIKE_IDENTIFIER);
        preparedStatement.setString(1, "%" + identifier + "%");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<RequestListDto> requests = new ArrayList<>();
        while (resultSet.next()) {
            RequestListDto request = new RequestListDto();
            request.setIdentifier(UUID.fromString(resultSet.getString(1)));
            request.setManufacturer(resultSet.getString(2));
            request.setModel(resultSet.getString(3));
            request.setSerialNumber(resultSet.getString(4));
            request.setClientSurname(resultSet.getString(5));
            request.setStatus(resultSet.getString(6));
            request.setDate_creation(resultSet.getTimestamp(7));
            requests.add(request);
        }
        return requests;
    }

    @Override
    public Optional<Long> findByIdentifier(UUID identifier) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_IDENTIFIER);
        preparedStatement.setObject(1, identifier);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(resultSet.getLong(1));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void bindWork(Long id, Long workId, Long employeeId) throws SQLException {
        List<Long> worksId = new ArrayList<>();
        worksId.add(workId);
        List<Long> employeesId = new ArrayList<>();
        employeesId.add(employeeId);
        m2m.saveRelatedIds("workrequest", "work_id", "request_id", id, worksId);
        m2m.saveRelatedIds("requestemployee", "employee_id", "request_id", id, employeesId);
    }

    @Override
    public List<RequestListDto> findByClient(Long clientId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_CLIENT_ID);
        preparedStatement.setLong(1, clientId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<RequestListDto> requests = new ArrayList<>();
        while (resultSet.next()) {
            RequestListDto request = new RequestListDto();
            request.setIdentifier(UUID.fromString(resultSet.getString(1)));
            request.setManufacturer(resultSet.getString(2));
            request.setModel(resultSet.getString(3));
            request.setSerialNumber(resultSet.getString(4));
            request.setStatus(resultSet.getString(5));
            request.setClientSurname(resultSet.getString(6));
            request.setDate_creation(resultSet.getTimestamp(7));
            requests.add(request);
        }
        return requests;
    }

    @Override
    public List<RequestListDto> findByEmployee(Long employeeId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_EMPLOYEE);
        preparedStatement.setLong(1, employeeId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<RequestListDto> requests = new ArrayList<>();
        while (resultSet.next()) {
            RequestListDto request = new RequestListDto();
            request.setIdentifier(UUID.fromString(resultSet.getString(1)));
            request.setManufacturer(resultSet.getString(2));
            request.setModel(resultSet.getString(3));
            request.setSerialNumber(resultSet.getString(4));
            request.setStatus(resultSet.getString(5));
            request.setDate_creation(resultSet.getTimestamp(6));
            requests.add(request);
        }
        return requests;
    }

    private Request createRequest(ResultSet resultSet) throws SQLException {
        Request request = new Request();
        request.setId(resultSet.getLong(1));
        request.setDescription(resultSet.getString(2));
        request.setDate_creation(resultSet.getTimestamp(3));
        request.setStatus(resultSet.getLong(4));
        request.setDevice(resultSet.getLong(5));
        request.setClient(resultSet.getLong(6));
        request.setIdentifier(UUID.fromString(resultSet.getString(7)));
        return request;
    }
}
