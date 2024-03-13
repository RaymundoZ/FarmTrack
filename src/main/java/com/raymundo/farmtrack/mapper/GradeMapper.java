package com.raymundo.farmtrack.mapper;

import com.raymundo.farmtrack.dto.GradeDto;
import com.raymundo.farmtrack.entity.GradeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GradeMapper {

    @Mapping(target = "user", ignore = true)
    GradeEntity toEntity(GradeDto gradeDto);

    @Mapping(source = "user.email", target = "user")
    GradeDto toDto(GradeEntity gradeEntity);
}
