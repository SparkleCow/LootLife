package com.sparklecow.LootLife.services.user;

import com.sparklecow.LootLife.entities.User;
import com.sparklecow.LootLife.models.user.UserImagesUpdateDto;
import com.sparklecow.LootLife.models.user.UserPasswordUpdateDto;
import com.sparklecow.LootLife.models.user.UserUpdateDto;
import com.sparklecow.LootLife.models.user.UsernameUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface UserService {
    Page<User> findAllUsers(Pageable pageable);
    Page<User> findUsersByUsername(String username, Pageable pageable);
    User findUserById(Long id);

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
