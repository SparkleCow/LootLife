package com.sparklecow.lootlife.services.mappers;

import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.user.UserRequestDto;
import com.sparklecow.lootlife.models.user.UserResponseDto;
import com.sparklecow.lootlife.models.user.UserUpdateDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public User userRequestDtoToUser(UserRequestDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setUsername(dto.username());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setAge(dto.age());
        user.setBirthDate(dto.birthDate());

        return user;
    }

    public UserResponseDto toDto(User user) {
        if (user == null) return null;

        List<String> roleNames = user.getRoles().stream()
                .map(x -> x.getRoleName().toString()).toList();

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getBannerImageUrl(),
                user.getAge(),
                user.getBirthDate(),
                user.getCreatedAt(),
                user.getLastModifiedAt(),
                user.isVerified(),
                user.isEnabled(),
                roleNames
        );
    }

    public void updateFromDto(UserUpdateDto dto, User user) {
        if (dto == null || user == null) return;

        if (dto.username() != null) user.setUsername(dto.username());
        if (dto.firstName() != null) user.setFirstName(dto.firstName());
        if (dto.lastName() != null) user.setLastName(dto.lastName());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.age() != null) user.setAge(dto.age());
        if (dto.birthDate() != null) user.setBirthDate(dto.birthDate());
        // Password and roles usually not updated here for security reasons
    }
}
