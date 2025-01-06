package ru.itis.service;

import ru.itis.dto.ValidateDto;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ValidationDataFormService {
    void validateForm();
    List<Optional<Date>> validateData(List<String> dates);
    List<String> getErrors();
}
