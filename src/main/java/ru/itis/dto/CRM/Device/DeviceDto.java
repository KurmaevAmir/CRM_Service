package ru.itis.dto.CRM.Device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {
    Long typeDeviceId;
    Long manufacturerId;
    Long specificationId;
    String serialNumber;
    String color;
}