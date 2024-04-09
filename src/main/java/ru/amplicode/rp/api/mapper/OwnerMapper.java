package ru.amplicode.rp.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.amplicode.rp.api.dto.OwnerDto;
import ru.amplicode.rp.model.Owner;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OwnerMapper {
    Owner toEntity(OwnerDto ownerDto);

    OwnerDto toDto(Owner owner);
}