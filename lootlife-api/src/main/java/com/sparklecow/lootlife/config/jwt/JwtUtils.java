package com.sparklecow.lootlife.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;

    public String generateToken(UserDetails userDetails){
        return generateToken(userDetails, new HashMap<>());
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

    public boolean validateToken(String token, UserDetails userDetails){
        if(isExpired(token)){
            return false;
        }
        if (!extractUsername(token).equals(userDetails.getUsername())) {
            return false;
        }

        Jwts.parser().setSigningKey(generateSignKey()).build().parseSignedClaims(token);
        return true;
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isExpired(String token){
        Date date = extractClaim(token, Claims::getExpiration);
        return date.before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims =  extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(generateSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    private Key generateSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
