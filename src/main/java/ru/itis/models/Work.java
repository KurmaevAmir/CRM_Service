package ru.itis.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Work {
    private Long id;
    private Long typeWork;
    private Integer price;
    private Integer warranty;
    private Long request;
    private List<Long> sparePart_id;
    private List<Long> employee_id;

    public Work() {
        sparePart_id = new ArrayList<>();
        employee_id = new ArrayList<>();
    }
}
