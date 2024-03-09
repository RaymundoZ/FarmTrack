package com.raymundo.farmtrack.mapper;

import com.raymundo.farmtrack.dto.UserInfoDto;
import com.raymundo.farmtrack.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserInfoMapper {

    UserInfoDto toDto(UserEntity userEntity);

    @Mapping(target = "isEnabled", constant = "true")
    @Mapping(target = "password", qualifiedByName = "encodePassword")
    UserEntity toEntity(UserInfoDto userInfoDto);

    @Named(value = "encodePassword")
    default String encodePassword(String password) {
        return passwordEncoder().encode(password);
    }

    default PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
