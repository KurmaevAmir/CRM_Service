package ru.itis.dto.CRM.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequestDto {
    private String description;
    private String serialNumber;
    private Long specificationId;
    private Long clientId;
    private InputStream file;
    private String originalFileName;
    private String contentType;
    private Long size;
}
