package com.sparklecow.LootLife.services.mappers;

import com.sparklecow.LootLife.entities.User;
import com.sparklecow.LootLife.models.user.UserRequestDto;
import com.sparklecow.LootLife.models.user.UserResponseDto;
import com.sparklecow.LootLife.models.user.UserUpdateDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id",            ignore = true)
    @Mapping(target = "createdAt",     ignore = true)
    @Mapping(target = "lastModifiedAt",ignore = true)
    @Mapping(target = "roles",         ignore = true)
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
