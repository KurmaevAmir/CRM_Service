package ru.itis.service;

import ru.itis.models.FileInfo;
import ru.itis.repositories.FileRepository;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Optional;

public class FileServiceImpl implements FileService {
    private FileRepository filesRepository;

    public FileServiceImpl(FileRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    @Override
    public void writeFileFromStorage(Long fileId, OutputStream outputStream) {
        Optional<FileInfo> fileInfoOptional;
        try {
            fileInfoOptional = filesRepository.findById(fileId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (fileInfoOptional.isPresent()) {
            FileInfo fileInfo = fileInfoOptional.get();
            File file = new File("/Users/amirkurmaev/Programming/2024/Java/CRM_Service/files/" + fileInfo.getStorageFileName() + "." + fileInfo.getType().split("/")[1]);
            try {
                Files.copy(file.toPath(), outputStream);
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public FileInfo getFileInfo(Long fileId) {
        Optional<FileInfo> fileInfoOptional;
        try {
            fileInfoOptional = filesRepository.findById(fileId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (fileInfoOptional.isPresent()) {
            return fileInfoOptional.get();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
