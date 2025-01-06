package ru.itis.dto.CRM.Specification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationAjaxDto {
    Long manufacturerId;
    Long typeDeviceId;
}
