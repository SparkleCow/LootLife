package com.sparklecow.lootlife.services.user;

import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.user.*;
import com.sparklecow.lootlife.repositories.RoleRepository;
import com.sparklecow.lootlife.repositories.UserRepository;
import com.sparklecow.lootlife.services.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

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
