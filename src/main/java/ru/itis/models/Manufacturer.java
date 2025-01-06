package ru.itis.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Manufacturer {
    private Long id;
    private String name;
    private List<Long> typesDevice;
}
