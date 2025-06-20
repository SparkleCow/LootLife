package com.sparklecow.lootlife.controllers;

import com.sparklecow.lootlife.models.user.AuthenticationRequestDto;
import com.sparklecow.lootlife.models.user.AuthenticationResponseDto;
import com.sparklecow.lootlife.models.user.UserRequestDto;
import com.sparklecow.lootlife.models.user.UserResponseDto;
import com.sparklecow.lootlife.services.user.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody @Valid AuthenticationRequestDto authenticationRequestDto){
        return ResponseEntity.ok(authenticationService.login(authenticationRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto userRequestDto){
        UserResponseDto userResponseDto = authenticationService.registerUser(userRequestDto);;
        URI location = URI.create("/users/" + userResponseDto.id());
        return ResponseEntity.created(location).body(userResponseDto);
    }
}
