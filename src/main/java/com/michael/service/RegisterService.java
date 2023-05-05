package com.michael.service;


import io.micronaut.security.token.generator.TokenGenerator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.thymeleaf.util.ClassLoaderUtils;

import java.nio.charset.Charset;

@Singleton
public class RegisterService {

    @Inject
    TokenGeneratorService generator;

    @Inject
    TokenGenerator tokenGenerator;



    //todo: вынести хост
    @SneakyThrows
    public String generateRegisterTemplate(String userOid){
        return IOUtils.toString(ClassLoaderUtils.findResourceAsStream(
                "html-templates/confirm.html"),
                Charset.forName("UTF-8")
                        ).replace("[[confirmationLink]]", "https://crutch-code.ru/api/reg/confirm?token="
                + generator.generate(
                        "auth", "register", userOid
                )
        );
    }
}
