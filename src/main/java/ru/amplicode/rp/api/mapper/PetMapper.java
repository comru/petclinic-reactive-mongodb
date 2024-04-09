package ru.amplicode.rp.api.mapper;

import org.mapstruct.*;
import ru.amplicode.rp.api.dto.PetDto;
import ru.amplicode.rp.model.Pet;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetMapper {
    Pet toEntity(PetDto petDto);

    PetDto toDto(Pet pet);

    Pet update(PetDto petDto, @MappingTarget Pet pet);
}