package com.sparklecow.lootlife.services.user;

import com.sparklecow.lootlife.entities.Role;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.role.RoleName;
import com.sparklecow.lootlife.models.user.AuthenticationRequestDto;
import com.sparklecow.lootlife.models.user.AuthenticationResponseDto;
import com.sparklecow.lootlife.models.user.UserRequestDto;
import com.sparklecow.lootlife.models.user.UserResponseDto;
import com.sparklecow.lootlife.repositories.RoleRepository;
import com.sparklecow.lootlife.repositories.UserRepository;
import com.sparklecow.lootlife.services.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {

        if (userRepository.existsByEmail(userRequestDto.email())) {
            throw new RuntimeException("Ya existe un usuario con ese correo.");
        }

        User user = userMapper.userRequestDtoToUser(userRequestDto);

        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        user.setPassword(passwordEncoder.encode(userRequestDto.password()));

        user.setRoles(Set.of(userRole));
        user.setEnabled(true);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto userAuthenticationDto) {
        return null;
    }
}
