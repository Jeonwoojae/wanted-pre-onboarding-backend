package com.example.wantedpreonboardingbackend.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final Environment env;

    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() +
                Long.parseLong(env.getProperty("token.access_expiration_time")));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, env.getProperty("token.secret"))
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(token).getBody().getSubject();
    }
}
