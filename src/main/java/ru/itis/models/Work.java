package ru.itis.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Work {
    private Long id;
    private Long typeWork;
    private Double price;
    private Integer warranty;
    private Long specification;
    private List<Long> employee_id;
    private List<Long> request;
}
