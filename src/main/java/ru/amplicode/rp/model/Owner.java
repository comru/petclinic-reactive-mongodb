package ru.amplicode.rp.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document
public class Owner extends Person {
    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    @Digits(fraction = 0, integer = 10)
    private String telephone;

}