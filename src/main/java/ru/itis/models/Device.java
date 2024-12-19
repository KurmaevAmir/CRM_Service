package ru.itis.models;

import lombok.*;

@Getter
@Setter
public class Device {
    private Long id;
    private String serialNumber;
    private String color;
    private Long specification;
}
