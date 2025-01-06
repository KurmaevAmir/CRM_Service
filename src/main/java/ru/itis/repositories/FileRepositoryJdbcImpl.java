package ru.itis.repositories;

import ru.itis.models.FileInfo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileRepositoryJdbcImpl implements FileRepository{
    private Connection connection;

    private final static String SQL_INSERT = "insert into file(storage_file_name, original_file_name, type, size) " +
            "values (?, ?, ?, ?)";

    private final static String SQL_SELECT_BY_ID = "select * from file where id = ?";
    private final static String SQL_SELECT_BY_STORAGE_FILE_NAME = "select * from file where storage_file_name = ?";

    public FileRepositoryJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {

    }

    @Override
    public void update(FileInfo entity) throws SQLException {

    }

    @Override
    public List<FileInfo> findAll() throws SQLException {
        return null;
    }

    @Override
    public void save(FileInfo entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        preparedStatement.setString(1, entity.getStorageFileName());
        preparedStatement.setString(2, entity.getOriginalFileName());
        preparedStatement.setString(3, entity.getType());
        preparedStatement.setLong(4, entity.getSize());
        preparedStatement.executeUpdate();
    }

    @Override
    public FileInfo findByStorageFileName(String uuid) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_STORAGE_FILE_NAME);
        preparedStatement.setString(1, uuid);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setId(resultSet.getLong(1));
            fileInfo.setStorageFileName(resultSet.getString(2));
            fileInfo.setOriginalFileName(resultSet.getString(3));
            fileInfo.setType(resultSet.getString(4));
            fileInfo.setSize(resultSet.getLong(5));
            return fileInfo;
        }
        return null;
    }

    @Override
    public Optional<FileInfo> findById(Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setId(resultSet.getLong(1));
            fileInfo.setStorageFileName(resultSet.getString(2));
            fileInfo.setOriginalFileName(resultSet.getString(3));
            fileInfo.setType(resultSet.getString(4));
            fileInfo.setSize(resultSet.getLong(5));
            return Optional.of(fileInfo);
        }
        return Optional.empty();
    }
}
