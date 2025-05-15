package com.sparklecow.LootLife.services.user;

import com.sparklecow.LootLife.models.user.UserAuthenticationDto;
import com.sparklecow.LootLife.models.user.UserRequestDto;

public interface AuthenticationService {
    void registerUser(UserRequestDto userRequestDto);
    String login(UserAuthenticationDto userAuthenticationDto);
}
