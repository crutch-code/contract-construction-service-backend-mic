package com.michael.utills.security;

import com.michael.entity.jpa.User;
import com.michael.repository.UserRepository;
import com.michael.utills.security.responses.CustomAuthResponse;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Singleton
public class AuthProviderUser implements AuthenticationProvider {

    @Inject
    UserRepository userRepository;

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        User user =
                userRepository.findByUserEmail(String.valueOf(authenticationRequest.getIdentity()))
                                .orElse(
                                        userRepository.findByUserPhone(String.valueOf(authenticationRequest.getIdentity())).orElse(null));
        if(user == null)
            return Flowable.just(AuthenticationResponse.failure("Пользователь с таким e-mail или номером телефона не найден"));

        if(!user.getIsConfirm()){
            return Flowable.just(AuthenticationResponse.failure("Пользователь не подтверждён"));
        }

        if (!user.getUserPassword().equals(authenticationRequest.getSecret()))
            return Flowable.just(AuthenticationResponse.failure("Неправильный пароль"));



        return Flowable.just(
                new CustomAuthResponse(
                        user.getId(),
                        user.getUserName(),
                        List.of(
                                user.getIsOperator() ? "IS_ADMIN": "DEFAULT_USER",
                                user.getIsContractor()? "IS_CONTRACTOR" : "NOT_CONTRACTOR"
                        ),
                        UUID.randomUUID().toString()
                )
        );
    }


}
