package ru.amplicode.rp.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.amplicode.rp.model.Pet;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Pet}
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public record PetDto(String id,
                     LocalDate birthDate,
                     @NotEmpty
                     String name,
                     @NotNull
                     String typeId,
                     @NotNull
                     String ownerId) implements Serializable {
}