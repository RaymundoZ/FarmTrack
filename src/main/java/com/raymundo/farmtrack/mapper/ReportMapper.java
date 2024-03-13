package com.raymundo.farmtrack.mapper;

import com.raymundo.farmtrack.dto.ReportDto;
import com.raymundo.farmtrack.entity.ReportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    ReportEntity toEntity(ReportDto reportDto);

    @Mapping(source = "product.name", target = "product")
    @Mapping(source = "user.email", target = "user")
    @Mapping(source = "product.measure", target = "measure")
    ReportDto toDto(ReportEntity reportEntity);
}
