package ru.itis.dto.CRM.Specification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationListDto {
    private Long id;
    private String manufacturer;
    private String model;
    private String article;
    private String typeDevice;
}
