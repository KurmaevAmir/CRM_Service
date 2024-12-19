package ru.itis.repositories;

import ru.itis.models.Passport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassportRepositoryJdbcImpl implements PassportRepository {
    private final Connection connection;

    private static final String SQL_SELECT_BY_ID = "select * from passport where id = ?";
    private static final String SQL_INSERT = "insert into passport (series, number, date_issue, issued, subdivision)" +
            "values (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "select * from passport";
    private static final String SQL_UPDATE = "update passport set series = ?, number = ?, date_issue = ?, " +
            "issued = ?, subdivision = ? where id = ?";
    private static final String SQL_DELETE = "delete from passport where id = ?";

    PassportRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Passport findById(Long id) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return createPassport(resultSet);
        }
        return null;
    }

    @Override
    public void save(Passport entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        prepareStatement(preparedStatement, entity);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Passport> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
        List<Passport> passports = new ArrayList<>();
        while(resultSet.next()){
            passports.add(createPassport(resultSet));
        }
        return passports;
    }

    @Override
    public void update(Passport entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
        prepareStatement(preparedStatement, entity);
        preparedStatement.setLong(6, entity.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Passport entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.executeUpdate();
    }

    private Passport createPassport(ResultSet resultSet) throws SQLException {
        Passport passport = new Passport();
        passport.setId(resultSet.getLong("id"));
        passport.setSeries(resultSet.getString("series"));
        passport.setNumber(resultSet.getString("number"));
        passport.setDate_issue(resultSet.getDate("date_issue"));
        passport.setIssued(resultSet.getString("issued"));
        passport.setSubdivision(resultSet.getString("subdivision"));
        return passport;
    }

    private void prepareStatement(PreparedStatement preparedStatement, Passport entity) throws SQLException {
        preparedStatement.setString(1, entity.getSeries());
        preparedStatement.setString(2, entity.getNumber());
        preparedStatement.setDate(3, entity.getDate_issue());
        preparedStatement.setString(4, entity.getIssued());
        preparedStatement.setString(5, entity.getSubdivision());
    }
}
