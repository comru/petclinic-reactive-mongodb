package ru.amplicode.rp.api.dto;

import ru.amplicode.rp.model.Pet;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Pet}
 */
public record PetDto(String id,
                     LocalDate birthDate,
                     String name,
                     String typeId,
                     String ownerId) implements Serializable {
}