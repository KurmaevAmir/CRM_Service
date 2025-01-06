package ru.itis.dto.CRM.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestListDto {
    private UUID identifier;
    private String manufacturer;
    private String model;
    private String serialNumber;
    private String clientSurname;
    private String status;
    private Timestamp date_creation;
}
