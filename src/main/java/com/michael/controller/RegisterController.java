package com.michael.controller;


import com.michael.entity.jpa.User;
import com.michael.entity.responses.AppResponseWithObject;
import com.michael.entity.responses.DefaultAppResponse;
import com.michael.entity.responses.exceptions.InternalExceptionResponse;
import com.michael.service.RegisterService;
import com.michael.service.TokenGeneratorService;
import com.nimbusds.jwt.JWTParser;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.data.annotation.Query;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecuredAnnotationRule;
import io.micronaut.views.View;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

@Controller("/api/reg")
@Tag(name = "Контроллер Регистрации",
        description = "Данный котроллер отвечает за регистрацию пользователей в систему Remote Rent System"
)
@Secured(SecuredAnnotationRule.IS_ANONYMOUS)
public class RegisterController extends BaseController{

    @Inject
    RegisterService registerService;

    @Inject
    TokenGeneratorService generatorService;
    public static final Logger registerLog = LoggerFactory.getLogger(RegisterController.class);


    @ExecuteOn(TaskExecutors.IO)
    @Operation(summary = "Эндпоинт для проверки почты или телефона на предмет существования аккаунта с такими данными")
    @Post(uri = "/check{?credential}",produces = MediaType.APPLICATION_JSON)
    public HttpResponse<AppResponseWithObject> checkEmail(
            @QueryValue Optional<String> credential
    ){
        try {
            return HttpResponse.ok(
                    responseService.successWithObject(
                            "Успешно проверено. Поле Source содержит результат",
                            userRepository.existsByUserEmailOrUserPhone(
                                credential.orElseThrow(
                                        ()->new RuntimeException("Передано пустое значение для credential")
                                )
                            )
                    )
            ).status(201);
        } catch (Exception e) {
            registerLog.error(e.getMessage());
            throw new InternalExceptionResponse("Error: " +e.getMessage() , responseService.error("error: " +e.getMessage()));
        }

    }


    @ExecuteOn(TaskExecutors.IO)
    @Operation(summary = "Эндпоинт для регистрации пользователя")
    @Post(uri = "/register",produces = MediaType.APPLICATION_JSON)
    public HttpResponse<DefaultAppResponse> register(
            @Body User credential
    ){
        try {
            credential.setUserRegDate(LocalDateTime.now(ZoneId.systemDefault()).toLocalDate());
            credential.setId(transactionalRepository.genOid().orElseThrow());
            credential.setIsOperator(false);
            credential.setIsConfirm(false);
            userRepository.save(credential);
            emailService.send(
                    credential.getUserEmail(),
                    "Подтверждение регистрации",
                    registerService.generateRegisterTemplate(credential.getId()));
            return HttpResponse.ok(
                    responseService.success()
            ).status(201);
        } catch (Exception e) {
            registerLog.error(e.getMessage());
            throw new InternalExceptionResponse("Error: " +e.getMessage() , responseService.error("error: " +e.getMessage()));
        }

    }

    @ExecuteOn(TaskExecutors.IO)
    @View("confirm-result")
    @Operation(summary = "Эндпоинт для регистрации пользователя")
    @Get(uri = "/confirm{?token}",produces = MediaType.TEXT_HTML)
    @Secured(SecuredAnnotationRule.IS_ANONYMOUS)
    public HttpResponse<Map<?, ?>> confirm(
            @QueryValue Optional<String> token
    ){
        try {
            if (!generatorService.valid(token.orElseThrow()) ){
                return HttpResponse.ok(CollectionUtils.mapOf("success", false));
            }
            userRepository.updateUserIsConfirmById(JWTParser.parse(token.orElseThrow()).getJWTClaimsSet().getStringClaim("uid"), true);

            return HttpResponse.ok(
                    CollectionUtils.mapOf("success", true)
            );

        } catch (Exception e) {
            registerLog.error(e.getMessage());
            throw new InternalExceptionResponse("Error: " +e.getMessage() , responseService.error("error: " +e.getMessage()));
        }

    }
}
