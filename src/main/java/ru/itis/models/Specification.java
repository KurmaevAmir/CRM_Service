package ru.itis.models;

import lombok.*;

@Getter
@Setter
public class Specification {
    private Long id;
    private Long typeDevice;
    private Long manufacturer;
    private String model;
    private String article;
}
