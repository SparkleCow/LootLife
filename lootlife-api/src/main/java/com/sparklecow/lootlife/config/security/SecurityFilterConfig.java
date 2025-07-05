package com.sparklecow.lootlife.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparklecow.lootlife.config.jwt.JwtFilter;
import com.sparklecow.lootlife.config.jwt.JwtUtils;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.user.CustomOAuth2User;
import com.sparklecow.lootlife.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilterConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/*").permitAll()
                        .requestMatchers("/task/*").authenticated()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorize")
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();
                            User user = customUser.getUser();
                            String jwt = jwtUtils.generateToken(user);

                            String redirectUrl = "http://localhost:4000/oauth-success?token=" + jwt;
                            response.sendRedirect(redirectUrl);
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            Map<String, String> errorResponse = new HashMap<>();
                            errorResponse.put("error", "OAuth2 login failed");
                            errorResponse.put("message", exception.getMessage());
                            response.setContentType("application/json");
                            new ObjectMapper().writeValue(response.getWriter(), errorResponse);
                        })
                )
                .authenticationProvider(authenticationProvider)
                .build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
