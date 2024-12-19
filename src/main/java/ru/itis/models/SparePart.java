package ru.itis.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class SparePart {
    private Long id;
    private String name;
    private Integer number;
    private String article;
    private List<Long> work;
}
