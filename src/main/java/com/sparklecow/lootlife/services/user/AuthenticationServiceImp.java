package com.sparklecow.lootlife.services.user;

import com.sparklecow.lootlife.config.jwt.JwtUtils;
import com.sparklecow.lootlife.entities.Role;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.role.RoleName;
import com.sparklecow.lootlife.models.user.AuthenticationRequestDto;
import com.sparklecow.lootlife.models.user.AuthenticationResponseDto;
import com.sparklecow.lootlife.models.user.UserRequestDto;
import com.sparklecow.lootlife.models.user.UserResponseDto;
import com.sparklecow.lootlife.repositories.RoleRepository;
import com.sparklecow.lootlife.repositories.UserRepository;
import com.sparklecow.lootlife.services.mappers.UserMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {

        if (userRepository.existsByEmail(userRequestDto.email())) {
            throw new RuntimeException("Ya existe un usuario con ese correo.");
        }

        User user = userMapper.userRequestDtoToUser(userRequestDto);

        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        user.setPassword(passwordEncoder.encode(userRequestDto.password()));

        user.setRoles(Set.of(userRole));
        user.setEnabled(true);

        return userMapper.toDto(userRepository.save(user));
    }

    public void sendValidation(User user) throws MessagingException {
        String token = saveAndGenerateToken(user);
        emailService.sendEmail(user.getEmail(), "Activación de cuenta", user.getUsername(),
                token, activationUrl, EmailTemplateName.ACTIVATE_ACCOUNT);
    }

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

    @Transactional
    public void validateToken(String tokenCode) throws MessagingException {
        Token token = tokenRepository.findByToken(tokenCode).orElseThrow(() -> new TokenNotFoundException("Token not found"));

        if(token.getValidatedAt()!=null){
            throw new InvalidTokenException("Token has been validated before");
        }
        if(token.getExpiresAt().isBefore(LocalDateTime.now())){
            sendValidation(token.getUser());
            throw new ExpiredTokenException("Token has expired");
        }
        token.setValidatedAt(LocalDateTime.now());
        User user = token.getUser();
        user.setEnabled(true);
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
