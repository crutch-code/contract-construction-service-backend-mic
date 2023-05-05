package com.michael.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.michael.entity.jpa.CategoryDictionary;
import com.michael.entity.jpa.Contract;
import com.michael.entity.jsonviews.JsonViewsCollector;
import com.michael.entity.responses.DefaultAppResponse;
import com.michael.entity.responses.exceptions.InternalExceptionResponse;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Sort;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecuredAnnotationRule;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Controller("/api/contract")
@Tag(name = "Контроллер контрактов",
        description = "Данный котроллер отвечает за создание контрактов пользователей, их удаление, перемещение в архив, изменение данных"
)
@Secured(SecuredAnnotationRule.IS_AUTHENTICATED)
@Validated
@SecurityScheme(name = "BearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "jwt")
public class ContractController extends BaseController{

    public static final Logger logger = LoggerFactory.getLogger(ContractController.class);

    @ExecuteOn(TaskExecutors.IO)
    @Operation(summary = "Получение списка предложений на выполнение")
    @Get(uri = "/get", produces = MediaType.APPLICATION_JSON_STREAM)
    @Secured(SecuredAnnotationRule.IS_ANONYMOUS)
    @JsonView({JsonViewsCollector.Entity.Default.class} )
    public HttpResponse<Page<Contract>> getContracts(
            @QueryValue @Nullable String city,
            @QueryValue @Nullable String country,
            @QueryValue @Nullable String category,
            @QueryValue @Nullable String subCategory,
            @QueryValue @Nullable Boolean by_user,
            @QueryValue @Nullable Sort.Order.Direction dir,
            @QueryValue @Nullable Integer page_num,
            @QueryValue @Nullable Integer page_size
    ){
        try{
            return HttpResponse.ok(contractRepository.getFiltered(
                    city == null? "" : city,
                    country == null? "" : country,
                    category == null? "" : category,
                    subCategory == null? "" : subCategory,
                    (by_user != null && by_user)? "" : "active",
                    (by_user != null && by_user)? getUserId() : null,
                    Sort.of(new Sort.Order("post_creation_date", dir, false)),
                    getPageable(page_num, page_size)
            ));
        }catch (Exception ex){
            logger.error(ex.getMessage());
            throw new InternalExceptionResponse(ex.getMessage(), responseService.error(ex.getMessage()));
        }
    }



    @ExecuteOn(TaskExecutors.IO)
    @Operation(summary = "Создать контракт")
    @Post(uri = "/create", produces = MediaType.APPLICATION_JSON_STREAM)
    @JsonView({JsonViewsCollector.Entity.Default.class} )
    @SecurityRequirement(name = "BearerAuth")
    public HttpResponse<DefaultAppResponse> addContract(
            @Body Contract adding
    ){
        try{
            if(adding.getId() == null){
                adding.setId(transactionalRepository.genOid().orElseThrow());
                addressRepository.save(adding.getContractAddress());
            }
            adding.setContractStatus("moderation");
            adding.setContractUpToDate(LocalDate.now(ZoneId.systemDefault()));
            adding.setContractCreatedDate(LocalDate.now(ZoneId.systemDefault()));
            adding.setId(transactionalRepository.genOid().orElseThrow(
                        () -> new RuntimeException("Ошибка генерации идентификатора")
                    )
            );
            adding.setContractUserCreator(getCurrentUser());
            contractRepository.save(adding);
            return HttpResponse.ok(
                    responseService.success(
                            "Контракт добавлен и отправлен на модерацию." +
                            (adding.getContractFiles() != null ? "На заметку! Добавлять файлы нужно по эндпоинту: " : "")
                    )
            );
        }catch (Exception ex){
            logger.error(ex.getMessage());
            throw new InternalExceptionResponse(ex.getMessage(),ex.getCause(), responseService.error(ex.getMessage()));
        }
    }

    @ExecuteOn(TaskExecutors.IO)
    @Operation(summary = "Обновить контракт")
    @Patch(uri = "/update", produces = MediaType.APPLICATION_JSON_STREAM)
    @SecurityRequirement(name = "BearerAuth")
    @JsonView({JsonViewsCollector.Entity.Default.class} )
    public HttpResponse<DefaultAppResponse> updateContract(
            @Body Contract adding
    ){
        try{
//            if (
//                    adding.getContractCategory().getCategoryMaster()==null ||
//                            categoryDictionaryRepository.valid(
//                                    adding.getContractCategory().getCategoryMaster(),
//                                    adding.getContractCategory().getCategorySub() == null ? "" : adding.getContractCategory().getCategorySub()
//                            )
//            )
//                throw new RuntimeException(
//                        "Не указана категория для работ данного типа или указана не верно," +
//                                " воспользуйтесь справочником: /api/contract/get/category"
//                );
//
//            if(adding.getContractPrice() == null)
//                throw new RuntimeException("Не указана цена данного контракта");
//
//            adding.setContractStatus("moderation");
//            adding.setContractUpToDate(LocalDate.now(ZoneId.systemDefault()));
//            adding.setContractCreatedDate(LocalDate.now(ZoneId.systemDefault()));
//            adding.setId(transactionalRepository.genOid().orElseThrow(
//                            () -> new RuntimeException("Ошибка генерации идентификатора")
//                    )
//            );
//            contractRepository.save(adding);
            return HttpResponse.ok(
                    responseService.success(
                            "Контракт добавлен и отправлен на модерацию." +
                                    (adding.getContractFiles() != null ? "На заметку! Добавлять файлы нужно по эндпоинту: " : "")
                    )
            );
        }catch (Exception ex){
            logger.error(ex.getMessage());
            throw new InternalExceptionResponse(ex.getMessage(),ex.getCause(),responseService.error(ex.getMessage()));
        }
    }


}
