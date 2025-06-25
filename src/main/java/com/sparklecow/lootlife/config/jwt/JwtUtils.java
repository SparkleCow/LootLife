package com.sparklecow.lootlife.config.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtUtils {

    public String generateToken(UserDetails userDetails){
        return "";
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant expirationTime = now.plusMillis(jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(extraClaims)
                .signWith(generateSignKey())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationTime))
                .compact();
    }

    public boolean validateToken(){
        return true;
    }



    public String extractUsername(String token){

    }
}
