package com.raymundo.farmtrack.mapper;

import com.raymundo.farmtrack.dto.HarvestRateDto;
import com.raymundo.farmtrack.entity.HarvestRateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HarvestRateMapper {

    @Mapping(target = "product", ignore = true)
    HarvestRateEntity toEntity(HarvestRateDto harvestRateDto);

    @Mapping(source = "product.name", target = "product")
    HarvestRateDto toDto(HarvestRateEntity harvestRateEntity);
}
