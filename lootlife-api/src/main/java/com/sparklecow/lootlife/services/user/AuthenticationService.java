package com.sparklecow.lootlife.services.user;

import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.user.AuthenticationRequestDto;
import com.sparklecow.lootlife.models.user.AuthenticationResponseDto;
import com.sparklecow.lootlife.models.user.UserRequestDto;
import com.sparklecow.lootlife.models.user.UserResponseDto;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    UserResponseDto registerUser(UserRequestDto userRequestDto) throws MessagingException;
    AuthenticationResponseDto login(AuthenticationRequestDto userAuthenticationDto);
    void sendValidation(User user) throws MessagingException;
    String saveAndGenerateToken(User user);
    String generateToken(Integer tokenLength);
    void validateToken(String tokenCode) throws MessagingException;
}
