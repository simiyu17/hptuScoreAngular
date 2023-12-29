package com.hptu.score.util;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.jwt.Claims;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;

@Singleton
public class AuthTokenUtil {


    public String generateJwt() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());

        return Jwt.issuer("https://simiyu.com/issuer")
                .upn("simiyu@gmail.com")
                .groups(new HashSet<>(Arrays.asList("users", "admins")))
                .claim(Claims.sub.name(), "simiyu@gmail.com")
                .claim(Claims.iat.name(), zdt.toInstant().toEpochMilli())
                .expiresAt(System.currentTimeMillis() + 13600)
                .sign();
    }
}
