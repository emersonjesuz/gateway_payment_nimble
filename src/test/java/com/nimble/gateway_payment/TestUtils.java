package com.nimble.gateway_payment;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.gateway_payment.user.UserEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TestUtils {
    public static String objectToJSON(Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generatedToken(UserEntity user) {
        var generateExpirationDate = LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-3"));
        Algorithm algorithm = Algorithm.HMAC256("123456");
        String token = JWT.create()
                .withIssuer("login-auth-api")
                .withSubject(String.valueOf(user.getId()))
                .withExpiresAt(generateExpirationDate)
                .sign(algorithm);
        return token;
    }
}
