package ru.itis.dto.CRM.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailDto {
    private String identifier;
    private String description;
    private Timestamp date;
    private String status;
    private String manufacturer;
    private String model;
    private String article;
    private String serialNumber;
    private String color;
    private String clientName;
    private String clientSurname;
    private String clientPatronymic;
    private String clientPhone;
    private String clientEmail;
    private Long file;
}
