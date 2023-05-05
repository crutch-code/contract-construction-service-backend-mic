package com.michael.controller;


import com.michael.entity.jpa.Address;
import com.michael.entity.jpa.CategoryDictionary;
import com.michael.entity.responses.exceptions.InternalExceptionResponse;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecuredAnnotationRule;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Controller("/api/dict")
@Tag(name = "Контроллер cправочник",
        description = "Получить информацию об актуальных адресах"
)
@Secured(SecuredAnnotationRule.IS_AUTHENTICATED)
@Validated
@SecurityScheme(name = "BearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "jwt")
public class DictionaryController extends BaseController {

    public static final Logger logger = LoggerFactory.getLogger(DictionaryController.class);

    @ExecuteOn(TaskExecutors.IO)
    @Operation(summary = "Получение списка доступных категорий")
    @Get(uri = "/get/categories", produces = MediaType.APPLICATION_JSON_STREAM)
    @Secured(SecuredAnnotationRule.IS_ANONYMOUS)
    public HttpResponse<Page<CategoryDictionary>> getCategories(
            @QueryValue @Nullable Integer page_num,
            @QueryValue @Nullable Integer page_size
    ){
        try{
            logger.info("Call: " + ServerRequestContext.currentRequest().get().getUri().toString());
            return HttpResponse.ok(
                    categoryDictionaryRepository.findAll(
                            getPageable(page_num, page_size)
                    )
            );

        }catch (Exception ex){
            logger.error(ex.getMessage());
            throw new InternalExceptionResponse(ex.getMessage(), responseService.error(ex.getMessage()));
        }
    }


    @ExecuteOn(TaskExecutors.IO)
    @Operation(summary = "Получение списка адресов, которые уже внесены в систему")
    @Get(uri = "/get/address", produces = MediaType.APPLICATION_JSON_STREAM)
    @Secured(SecuredAnnotationRule.IS_ANONYMOUS)
    public HttpResponse<Page<Address>> getAddress(
            @QueryValue Optional<String> city,
            @QueryValue Optional<String> country,
            @QueryValue Optional<String> street,
            @QueryValue Optional<Integer> building,
            @QueryValue @Nullable Integer apart,
            @QueryValue @Nullable Integer page_num,
            @QueryValue @Nullable Integer page_size
    ){
        try{
            logger.info("Call: " + ServerRequestContext.currentRequest().get().getUri().toString());
            return HttpResponse.ok(
                    addressRepository.getFiltered(
                            country.orElse(""),
                            city.orElse(""),
                            street.orElse(""),
                            building.orElse(null),
                            apart,
                            getPageable(page_num, page_size)
                    )
            );

        }catch (Exception ex){
            logger.error(ex.getMessage());
            throw new InternalExceptionResponse(ex.getMessage(), responseService.error(ex.getMessage()));
        }
    }
}
