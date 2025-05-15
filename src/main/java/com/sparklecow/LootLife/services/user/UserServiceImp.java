package com.sparklecow.LootLife.services.user;

import com.sparklecow.LootLife.entities.User;
import com.sparklecow.LootLife.models.user.*;
import com.sparklecow.LootLife.repositories.RoleRepository;
import com.sparklecow.LootLife.repositories.UserRepository;
import com.sparklecow.LootLife.services.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService, AuthenticationService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public void registerUser(UserRequestDto userRequestDto) {
        User user = userMapper.userRequestDtoToUser(userRequestDto);
        user.setRoles(new HashSet<>());
    }

    @Override
    public String login(UserAuthenticationDto userAuthenticationDto) {
        return "";
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return null;
    }

    @Override
    public Page<User> findUsersByUsername(String username, Pageable pageable) {
        return null;
    }

    @Override
    public User findUserById(Long id) {
        return null;
    }

    @Override
    public User updateUserById(Long id, UserUpdateDto userUpdateDto) {
        return null;
    }

    @Override
    public User updateUsernameById(Long id, UsernameUpdateDto usernameUpdateDto) {
        return null;
    }

    @Override
    public User updatePasswordById(Long id, UserPasswordUpdateDto userPasswordUpdateDto) {
        return null;
    }

    @Override
    public User updateImagesById(Long id, UserImagesUpdateDto userImagesUpdateDto) {
        return null;
    }

    @Override
    public User updateUserByIdAsUser(Authentication authentication, UserUpdateDto userUpdateDto) {
        return null;
    }

    @Override
    public User updateUsernameByIdAsUser(Authentication authentication, UsernameUpdateDto usernameUpdateDto) {
        return null;
    }

    @Override
    public User updatePasswordByIdAsUser(Authentication authentication, UserPasswordUpdateDto userPasswordUpdateDto) {
        return null;
    }

    @Override
    public User updateImagesByIdAsUser(Authentication authentication, UserImagesUpdateDto userImagesUpdateDto) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }
}
