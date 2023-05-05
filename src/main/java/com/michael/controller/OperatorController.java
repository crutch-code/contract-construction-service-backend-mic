package com.michael.controller;


import com.michael.entity.responses.DefaultAppResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecuredAnnotationRule;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;

@Controller("/api/auth")
@Tag(name = "Контроллер для оператора",
        description = "Содержит в себе методы для модерирования заказов, исполнителей. Так же методы для конфигурации отображаемые категории"
)
@Secured(SecuredAnnotationRule.IS_AUTHENTICATED)
@RolesAllowed("IS_ADMIN")
@Validated
@SecurityScheme(name = "BearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "jwt")
public class OperatorController {




}
