package ru.itis.service;

import ru.itis.models.FileInfo;

import java.io.OutputStream;
import java.sql.SQLException;

public interface FileService {
    void writeFileFromStorage(Long fileId, OutputStream outputStream);
    FileInfo getFileInfo(Long fileId);
}
