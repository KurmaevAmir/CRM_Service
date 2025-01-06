package ru.itis.dto.CRM.Manufacturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerAddAjaxDto {
    private String manufacturerName;
    private String deviceType;
}
