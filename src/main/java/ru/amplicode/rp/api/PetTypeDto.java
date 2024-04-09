package ru.amplicode.rp.api;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link ru.amplicode.rp.model.PetType}
 */
public record PetTypeDto(@NotNull String name, String id) implements Serializable {
}