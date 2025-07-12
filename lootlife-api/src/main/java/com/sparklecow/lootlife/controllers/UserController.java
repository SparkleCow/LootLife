package com.sparklecow.lootlife.controllers;

import com.sparklecow.lootlife.models.user.UserResponseDto;
import com.sparklecow.lootlife.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDto> findLoggedUser(Authentication authentication){
        return ResponseEntity.ok(userService.findLoggedUser(authentication));
    }
}
