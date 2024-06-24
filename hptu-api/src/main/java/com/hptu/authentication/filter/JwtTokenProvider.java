package com.hptu.authentication.filter;

import com.hptu.authentication.domain.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

    private JwtTokenProvider(){}

    public static  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SecurityConstants.SECRET.getBytes())
                .build().parseClaimsJws(token).getBody();
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public static String createToken(AppUser user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("user_id", user.getId())
                .claim("user_full_name", user.getUserFullName())
                .claim("roles", new HashSet<>(Collections.singletonList(user.getRole())))
                .claim("force_change_password", user.isForceChangePass())
                .issuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes())
                .compact();
    }

    private static String createTokenFromClaims(Claims claims) {
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes()).compact();
    }
}
