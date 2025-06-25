package com.sparklecow.lootlife.config.jwt;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * This class allows to write data in application.properties file and use it as a bean.
 * Lombok's @Data annotation generates getters, setters, toString, equals, and hashCode methods.*/
@Configuration
@ConfigurationProperties(prefix = "application.security.jwt")
@Validated
@Data
public class JwtProperties {
    @NotNull
    private String secretKey;
    @NotNull
    private Long expiration;
}