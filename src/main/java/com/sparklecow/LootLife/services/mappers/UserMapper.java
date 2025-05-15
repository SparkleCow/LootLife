package com.sparklecow.LootLife.services.mappers;

import com.sparklecow.LootLife.entities.User;
import com.sparklecow.LootLife.models.user.UserRequestDto;
import com.sparklecow.LootLife.models.user.UserResponseDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id",            ignore = true)
    @Mapping(target = "createdAt",     ignore = true)
    @Mapping(target = "lastModifiedAt",ignore = true)
    @Mapping(target = "roles",         ignore = true)
    User userRequestDtoToUser(UserRequestDto dto);

    UserResponseDto toDto(User user);
    /**
     * Actualiza una entidad existente con valores no nulos del DTO.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",            ignore = true)
    @Mapping(target = "createdAt",     ignore = true)
    @Mapping(target = "lastModifiedAt",ignore = true)
    void updateFromDto(UserRequestDto dto, @MappingTarget User entity);
}
