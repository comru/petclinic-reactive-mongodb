package ru.amplicode.rp.api.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link ru.amplicode.rp.model.Owner}
 */
public record OwnerDto(String id,
                       String firstName,
                       String lastName,
                       @NotBlank String address,
                       @NotBlank String city,
                       @Digits(integer = 10, fraction = 0) @NotBlank String telephone) implements Serializable {
}