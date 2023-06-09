package com.michael.utills.security;

import com.nimbusds.jwt.JWTClaimsSet;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.config.TokenConfiguration;
import io.micronaut.security.token.jwt.generator.claims.JWTClaimsSetGenerator;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;

@Singleton
@Replaces(JWTClaimsSetGenerator.class)
public class CustomJWTClaimsSetGenerator extends JWTClaimsSetGenerator {
    /**
     * @param tokenConfiguration       Token Configuration
     * @param applicationConfiguration The application configuration
     */

    public CustomJWTClaimsSetGenerator(TokenConfiguration tokenConfiguration, ApplicationConfiguration applicationConfiguration) {
        super(tokenConfiguration, null, null, applicationConfiguration);
    }

    @SneakyThrows
    @Override
    protected void populateWithAuthentication(JWTClaimsSet.Builder builder, Authentication authentication) {
        super.populateWithAuthentication(builder, authentication);
        builder.claim("uid", ((CustomAuthentication) authentication).getUid());
        builder.claim("name", authentication.getName());
        builder.claim("session", ((CustomAuthentication) authentication).getSessionUUID());
        builder.claim("roles",  authentication.getRoles());
        builder.claim("attributes", authentication.getAttributes());
    }
}
