package ru.itis.dto.CRM.Client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientAjaxDto {
    private String name;
    private String surname;
    private String patronymic;
}
