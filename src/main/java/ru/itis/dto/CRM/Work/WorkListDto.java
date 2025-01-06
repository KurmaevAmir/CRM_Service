package ru.itis.dto.CRM.Work;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkListDto {
    private Long id;
    private String typeWork;
    private Double price;
    private Integer warranty;
    private String manufacturer;
    private String model;
    private String article;
}
