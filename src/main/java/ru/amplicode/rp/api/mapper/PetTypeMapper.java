package ru.amplicode.rp.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.amplicode.rp.api.PetTypeDto;
import ru.amplicode.rp.model.PetType;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetTypeMapper {
    PetType toEntity(PetTypeDto petTypeDto);

    PetTypeDto toDto(PetType petType);
}