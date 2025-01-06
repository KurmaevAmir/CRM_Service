package ru.itis.repositories;

import ru.itis.models.FileInfo;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends CrudRepository<FileInfo>{
    FileInfo findByStorageFileName(String uuid) throws SQLException;
    Optional<FileInfo> findById(Long id) throws SQLException;
}
