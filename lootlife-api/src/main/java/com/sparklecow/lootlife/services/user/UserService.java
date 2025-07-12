package com.sparklecow.lootlife.services.user;

import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface UserService
{
    Page<User> findAllUsers(Pageable pageable);
    Page<User> findUsersByUsername(String username, Pageable pageable);
    User findUserById(Long id);

    //Method for logged users. It allows to check their information, profile image and background image
    UserResponseDto findLoggedUser(Authentication authentication);

    //These methods are only for admins who can introduce and specific ID.
    User updateUserById(Long id, UserUpdateDto userUpdateDto);
    User updateUsernameById(Long id, UsernameUpdateDto usernameUpdateDto);
    User updatePasswordById(Long id, UserPasswordUpdateDto userPasswordUpdateDto);
    User updateImagesById(Long id, UserImagesUpdateDto userImagesUpdateDto);

    //These methods are for users who can change their information once they are logged.
    User updateUserByIdAsUser(Authentication authentication, UserUpdateDto userUpdateDto);
    User updateUsernameByIdAsUser(Authentication authentication, UsernameUpdateDto usernameUpdateDto);
    User updatePasswordByIdAsUser(Authentication authentication, UserPasswordUpdateDto userPasswordUpdateDto);
    User updateImagesByIdAsUser(Authentication authentication, UserImagesUpdateDto userImagesUpdateDto);

    void deleteUserById(Long id);
}
