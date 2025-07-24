package com.sparklecow.lootlife.controllers;

import com.sparklecow.lootlife.models.token.TokenValidationRequest;
import com.sparklecow.lootlife.models.user.AuthenticationRequestDto;
import com.sparklecow.lootlife.models.user.AuthenticationResponseDto;
import com.sparklecow.lootlife.models.user.UserRequestDto;
import com.sparklecow.lootlife.models.user.UserResponseDto;
import com.sparklecow.lootlife.services.user.AuthenticationService;
import com.sparklecow.lootlife.services.user.TokenAuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;
    private final TokenAuthenticationService tokenAuthenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody @Valid AuthenticationRequestDto authenticationRequestDto){
        return ResponseEntity.ok(authenticationService.login(authenticationRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto userRequestDto) throws MessagingException {
        UserResponseDto userResponseDto = authenticationService.registerUser(userRequestDto);
        URI location = URI.create("/users/" + userResponseDto.id());
        return ResponseEntity.created(location).body(userResponseDto);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody @Valid TokenValidationRequest request) {
        log.info("Token {}", request);
        try {
            tokenAuthenticationService.validateToken(request.token());
            return ResponseEntity.ok().build();
        } catch (RuntimeException | MessagingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
