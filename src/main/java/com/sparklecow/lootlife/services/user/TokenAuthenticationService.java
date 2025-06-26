package com.sparklecow.lootlife.services.user;

import com.sparklecow.lootlife.entities.User;
import jakarta.mail.MessagingException;


public interface TokenAuthenticationService {

    void sendValidation(User user) throws MessagingException;
    String saveAndGenerateToken(User user);
    String generateToken(Integer tokenLength);
    void validateToken(String tokenCode) throws MessagingException;
}
