package com.sparklecow.lootlife.services.user;

import com.sparklecow.lootlife.models.user.AuthenticationRequestDto;
import com.sparklecow.lootlife.models.user.AuthenticationResponseDto;
import com.sparklecow.lootlife.models.user.UserRequestDto;
import com.sparklecow.lootlife.models.user.UserResponseDto;

public interface AuthenticationService {
    UserResponseDto registerUser(UserRequestDto userRequestDto);
    AuthenticationResponseDto login(AuthenticationRequestDto userAuthenticationDto);
}
