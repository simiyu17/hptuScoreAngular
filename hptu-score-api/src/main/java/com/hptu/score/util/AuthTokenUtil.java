package com.hptu.score.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hptu.score.dto.UserDto;
import com.hptu.score.entity.User;
import com.hptu.score.exception.UserNotAuthenticatedException;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Singleton
public class AuthTokenUtil {


    public String generateJwt(User user)  {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());

        try {
            return Jwt.issuer("https://simiyu.com/issuer")
                    .upn(user.getUsername())
                    .groups(new HashSet<>(Collections.singletonList(user.getRole())))
                    .claim("user", new ObjectMapper().writeValueAsString(new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getDesignation(), user.getCellPhone(), user.isActive(), user.isAdmin())))
                    .claim(Claims.sub.name(), user.getUsername())
                    .claim(Claims.iat.name(), zdt.toInstant().toEpochMilli())
                    .expiresAt(System.currentTimeMillis() + 13600)
                    .sign();
        } catch (JsonProcessingException e) {
            throw new UserNotAuthenticatedException(e.getMessage());
        }
    }
}
