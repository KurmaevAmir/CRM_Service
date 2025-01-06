package ru.itis.models;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Passport {
    private Long id;
    private String series;
    private String number;
    private Date date_issue;
    private String issued;
    private String subdivision;
}
