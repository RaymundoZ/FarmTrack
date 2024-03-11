package com.raymundo.farmtrack.mapper;

import com.raymundo.farmtrack.dto.ProductDto;
import com.raymundo.farmtrack.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ProductEntity toEntity(ProductDto productDto);

    ProductDto toDto(ProductEntity productEntity);
}
