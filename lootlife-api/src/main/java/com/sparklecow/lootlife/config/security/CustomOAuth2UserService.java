package com.sparklecow.lootlife.config.security;

import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.user.CustomOAuth2User;
import com.sparklecow.lootlife.repositories.UserRepository;
import com.sparklecow.lootlife.services.stats.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StatsService statsService;

    /*This method receives an OAuth2UserRequest and extract its information*/
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = oauth2User.getAttribute("email");
        String username = oauth2User.getAttribute("login");

        //Here we validate if the registrationId is Github.
        //TODO uses another registrationId such as Google or Facebook
        String githubRegistrationId = "github";
        /*In Github, if the account is not public, we cant receive the email in a traditional way, we require to do a request
        * at another Github endpoint with the token in order to receive the email*/
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
                    .stats(statsService.createStats())
                    .build();
            userRepository.save(user);
        }
        //CustomOauth2User is a wrapper that contains oauth2User and user information.
        return new CustomOAuth2User(oauth2User, user);
    }
}