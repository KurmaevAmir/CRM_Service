package ru.itis.dto.CRM.Manufacturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerBindingDto {
    private String manufacturerId;
    private String deviceTypeId;
}
