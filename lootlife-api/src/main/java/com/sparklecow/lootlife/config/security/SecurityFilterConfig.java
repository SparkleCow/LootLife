package com.sparklecow.lootlife.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparklecow.lootlife.config.jwt.JwtFilter;
import com.sparklecow.lootlife.config.jwt.JwtUtils;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.user.CustomOAuth2User;
import com.sparklecow.lootlife.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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
@Slf4j
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
                        .requestMatchers("/login/oauth2/*").permitAll()
                        .requestMatchers("/oauth-success").permitAll()
                        .requestMatchers("/auth/**", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();
                            User user = customUser.getUser();
                            String jwt = jwtUtils.generateToken(user);
                            log.info("jwt {}", jwt);
                            String redirectUrl = "http://localhost:4200/auth/oauth-success?token=" + jwt;
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
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Code 401
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            Map<String, Object> errorDetails = new HashMap<>();
                            errorDetails.put("message", "Acceso no autorizado. Por favor inicie sesión o use OAuth2.");
                            errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                            new ObjectMapper().writeValue(response.getWriter(), errorDetails);
                        })
                        // This handler is used when an authenticated user is not allowed to access to a resource (Code 403)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Código 403
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            Map<String, Object> errorDetails = new HashMap<>();
                            errorDetails.put("message", "Acceso denegado. No tiene los permisos necesarios.");
                            errorDetails.put("status", HttpServletResponse.SC_FORBIDDEN);
                            new ObjectMapper().writeValue(response.getWriter(), errorDetails);
                        })
                )
                .formLogin(AbstractHttpConfigurer::disable)
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
