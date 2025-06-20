package com.sparklecow.lootlife.services.mappers;

import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.user.UserRequestDto;
import com.sparklecow.lootlife.models.user.UserResponseDto;
import com.sparklecow.lootlife.models.user.UserUpdateDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id",            ignore = true)
    @Mapping(target = "createdAt",     ignore = true)
    @Mapping(target = "lastModifiedAt",ignore = true)
    @Mapping(target = "roles",         ignore = true)
    @Mapping(target = "password",      ignore = true)
    User userRequestDtoToUser(UserRequestDto userRequestDto);

    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(Role::getName).toList())")
    UserResponseDto toDto(User user);
    /**
     * This updates an entity with not null values from DTO
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",            ignore = true)
    @Mapping(target = "createdAt",     ignore = true)
    @Mapping(target = "lastModifiedAt",ignore = true)
    void updateFromDto(UserUpdateDto userUpdateDto, @MappingTarget User user);
}
