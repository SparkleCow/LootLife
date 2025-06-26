package com.sparklecow.lootlife.services.user;

import com.sparklecow.lootlife.config.jwt.JwtUtils;
import com.sparklecow.lootlife.entities.Role;
import com.sparklecow.lootlife.entities.Token;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.email.EmailTemplateName;
import com.sparklecow.lootlife.models.role.RoleName;
import com.sparklecow.lootlife.models.user.AuthenticationRequestDto;
import com.sparklecow.lootlife.models.user.AuthenticationResponseDto;
import com.sparklecow.lootlife.models.user.UserRequestDto;
import com.sparklecow.lootlife.models.user.UserResponseDto;
import com.sparklecow.lootlife.repositories.RoleRepository;
import com.sparklecow.lootlife.repositories.TokenRepository;
import com.sparklecow.lootlife.repositories.UserRepository;
import com.sparklecow.lootlife.services.email.EmailService;
import com.sparklecow.lootlife.services.mappers.UserMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService, TokenAuthenticationService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    @Value("${application.mailing.activation-url}")
    private String activationUrl;

    @Override
    @Transactional
    public UserResponseDto registerUser(UserRequestDto userRequestDto) throws MessagingException {

        if (userRepository.existsByEmail(userRequestDto.email())) {
            throw new RuntimeException("Ya existe un usuario con ese correo.");
        }

        User user = userMapper.userRequestDtoToUser(userRequestDto);

        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        user.setPassword(passwordEncoder.encode(userRequestDto.password()));
        user.setRoles(Set.of(userRole));
        UserResponseDto userResponseDto = userMapper.toDto(userRepository.save(user));
        sendValidation(user);
        return userResponseDto;
    }

    @Override
    @Transactional
    public void sendValidation(User user) throws MessagingException {
        String token = saveAndGenerateToken(user);
        emailService.sendEmail(user.getEmail(), "Activación de cuenta", user.getUsername(),
                token, activationUrl, EmailTemplateName.ACTIVATE_ACCOUNT);
    }

    @Override
    @Transactional
    public String saveAndGenerateToken(User user) {
        String tokenInfo = generateToken(6);
        Token token = Token.builder()
                .token(tokenInfo)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenRepository.save(token);
        return tokenInfo;
    }

    @Override
    @Transactional
    public String generateToken(Integer tokenLength) {
        String characters = "0123456789";
        StringBuilder token = new StringBuilder();
        SecureRandom sr = new SecureRandom();
        for (int i = 0; i < tokenLength; i++) {
            int randomIndex = sr.nextInt(characters.length());
            token.append(characters.charAt(randomIndex));
        }
        return token.toString();
    }

    @Override
    @Transactional
    public void validateToken(String tokenCode) throws MessagingException {
        Token token = tokenRepository.findByToken(tokenCode).orElseThrow(() -> new RuntimeException("Token not found"));

        if(token.getValidatedAt()!=null){
            throw new RuntimeException("Token has been validated before");
        }
        if(token.getExpiresAt().isBefore(LocalDateTime.now())){
            sendValidation(token.getUser());
            throw new RuntimeException("Token has expired");
        }
        token.setValidatedAt(LocalDateTime.now());
        User user = token.getUser();
        user.setEnabled(true);
        user.setVerified(true);
        userRepository.save(user);
        tokenRepository.save(token);
    }


    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequestDto.username(), authenticationRequestDto.password());

        UserDetails userDetails  = (UserDetails) authenticationManager.authenticate(authentication).getPrincipal();

        return new AuthenticationResponseDto(jwtUtils.generateToken(userDetails));
    }
}
