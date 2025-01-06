package ru.itis.service;

import ru.itis.dto.ValidateDto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ValidationDataFormServiceImpl implements ValidationDataFormService {
    private ValidateDto dto;
    private List<String> errors = new ArrayList<>();
    private boolean isEmployee;

    public ValidationDataFormServiceImpl(ValidateDto dto, boolean isEmployee) {
        this.dto = dto;
        this.isEmployee = isEmployee;
    }

    @Override
    public void validateForm() {
        String name = dto.getName();
        String surname = dto.getSurname();
        String patronymic = dto.getPatronymic();
        String dateOfBirth = dto.getDate_of_birth();
        String phoneNumber = dto.getPhone_number();
        String email = dto.getEmail();
        String passportSeries = dto.getPassportSeries();
        String passportNumber = dto.getPassportNumber();
        String passportIssueDate = dto.getPassportIssueDate();
        String passportIssued = dto.getPassportIssued();
        String passportSubdivision = dto.getPassportSubdivision();
        String snils = dto.getSnils();
        String inn = dto.getInn();
        String password = dto.getPassword();
        String passwordConfirm = dto.getPasswordConfirm();

        if (isEmpty(name)) errors.add("Имя должно быть заполнено.");
        if (isEmpty(surname)) errors.add("Фамилия должна быть заполнена.");
        if (isEmpty(dateOfBirth)) errors.add("Дата рождения должна быть заполнена.");
        if (isEmpty(phoneNumber)) errors.add("Номер телефона должен быть заполнен.");
        if (isEmpty(email)) errors.add("Email должен быть заполнен.");
        if (isEmpty(passportSeries)) errors.add("Серия паспорта должна быть заполнена.");
        if (isEmpty(passportNumber)) errors.add("Номер паспорта должен быть заполнен.");
        if (isEmpty(passportIssueDate)) errors.add("Дата выдачи паспорта должна быть заполнена.");
        if (isEmpty(passportIssued)) errors.add("Поле \"Кем выдано\" должно быть заполнено.");
        if (isEmpty(passportSubdivision)) errors.add("Поле \"Подразделение\" должно быть заполнено.");
        if (isEmpty(password)) errors.add("Пароль должен быть заполнен.");
        if (isEmpty(passwordConfirm)) errors.add("Подтверждение пароля должно быть заполнено.");

        if (isEmployee) {
            if (isEmpty(snils)) errors.add("СНИЛС должен быть заполнен.");
            if (isEmpty(inn)) errors.add("ИНН должен быть заполнен.");
            if (snils.length() != 11) errors.add("СНИЛС должен быть длиной 11 символов.");
            if (inn.length() != 12) errors.add("ИНН должен быть длиной 12 символов.");
        }
        if (!isEmpty(password) && !password.equals(passwordConfirm)) {
            errors.add("Пароль и подтверждение пароля не совпадают.");
        }

        if (name.length() > 50) errors.add("Длина имени не должна превышать 50 символов.");
        if (surname.length() > 50) errors.add("Длина фамилии не должна превышать 50 символов.");
        if (patronymic.length() > 50) errors.add("Длина отчества не должна превышать 50 символов.");
        if (phoneNumber.length() > 15) errors.add("Длина номера телефона не должна превышать 15 символов.");
        if (email.length() > 100) errors.add("Длина email не должна превышать 100 символов.");
        if (passportSeries.length() != 4) errors.add("Серия паспорта должна быть длиной 4 символа.");
        if (passportNumber.length() != 6) errors.add("Номер паспорта должен быть длиной 6 символов.");
        if (passportIssued.length() > 255) errors.add("Длина названия паспорта не должна превышать 255 символов.");
        if (passportSubdivision.length() != 6) errors.add("Длина кода подразделения паспорта должна составлять 6 символов.");
        if (password.length() < 8) errors.add("Пароль должен быть длиной не менее 8 символов.");
    }

    @Override
    public List<Optional<Date>> validateData(List<String> dates) {
        List<Optional<Date>> resultDates = new ArrayList<>();
        for (String date : dates) {
            if (!isEmpty(date)) {
                try {
                    resultDates.add(Optional.of(Date.valueOf(date)));
                } catch (IllegalArgumentException e) {
                    resultDates.add(Optional.empty());
                    errors.add("Дата должна быть в формате YYYY-MM-DD.");
                }
            }
        }
        return resultDates;
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }
}
