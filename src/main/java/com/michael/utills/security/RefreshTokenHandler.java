package com.michael.utills.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.michael.entity.responses.exceptions.InternalExceptionResponse;
import com.michael.repository.UserRepository;
import com.michael.service.ErrorService;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;


import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;


public class RefreshTokenHandler implements RefreshTokenPersistence {

    public static final Logger log = LoggerFactory.getLogger(RefreshTokenHandler.class);

    @Inject
    ErrorService errorService;

    @Inject
    public UserRepository repository;

    @Override
    public void persistToken(RefreshTokenGeneratedEvent event) {
        log.info("token generated: " + event.getRefreshToken());
    }

    @SneakyThrows
    @Override
    public Publisher<Authentication> getAuthentication(String refreshToken) {
        try {
            JWT jwt = JWTParser.parse(refreshToken);
            ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
            return Flowable.just(new CustomAuthentication(
                        jwt.getJWTClaimsSet().getStringClaim("uid"),
                        jwt.getJWTClaimsSet().getStringClaim("name"),
                        om.readValue(jwt.getJWTClaimsSet().getClaim("roles").toString(), Collection.class),
                        om.readValue(jwt.getJWTClaimsSet().getClaim("attributes").toString(), Map.class),
                        jwt.getJWTClaimsSet().getStringClaim("session")
                    )
            );
        }catch (Exception e){
            throw new InternalExceptionResponse(errorService.error(e.getMessage()));
        }
    }

    @SneakyThrows
    public String getUserName(String token) {
        try {
            JWT jwt = JWTParser.parse(token);
            return jwt.getJWTClaimsSet().getStringClaim("login");

        } catch (Exception e) {
            throw new InternalExceptionResponse(errorService.error(e.getMessage()));
        }
    }
}
