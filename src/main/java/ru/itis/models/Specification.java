package ru.itis.models;

import lombok.*;

@Getter
@Setter
public class Specification {
    private Long id;
    private String manufacturer;
    private String model;
    private String article;
    private Long typeDevice;
}
