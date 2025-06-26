package com.sparklecow.lootlife.config.security;

import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.user.CustomOAuth2User;
import com.sparklecow.lootlife.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String githubRegistrationId = "github";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = oauth2User.getAttribute("email"); // No se ejecuta asi directamente
        String username = oauth2User.getAttribute("login"); // GitHub: usa "login", Google usa "name"

        // 🔍 Si GitHub no devuelve el email directamente, obtenlo desde el endpoint de emails
        if (email == null && githubRegistrationId.equalsIgnoreCase(registrationId)) {
            String token = userRequest.getAccessToken().getTokenValue();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getBody() != null) {
                for (Map<String, Object> emailEntry : response.getBody()) {
                    if (Boolean.TRUE.equals(emailEntry.get("primary"))) {
                        email = (String) emailEntry.get("email");
                        break;
                    }
                }
            }
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("No se pudo obtener el email del proveedor OAuth2.");
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            user.setUsername(username);
            user.setOauth(true);
            user.setOauth2Provider(registrationId);
            user.setEnabled(true);
            user.setVerified(true);
            userRepository.save(user);
        } else {
            user = User.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .oauth(true)
                    .oauth2Provider(registrationId)
                    .isEnabled(true)
                    .isVerified(true)
                    .build();
            userRepository.save(user);
        }
        return new CustomOAuth2User(oauth2User, user);
    }
}