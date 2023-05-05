package com.michael.controller;


import com.michael.entity.jpa.User;
import com.michael.repository.*;
import com.michael.service.EmailService;
import com.michael.service.FilesService;
import com.michael.service.ResponseService;
import com.michael.service.UserLogoutService;
import com.michael.utills.security.CustomAuthentication;
import io.micronaut.context.annotation.Value;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.MediaType;
import io.micronaut.security.utils.SecurityService;
import jakarta.inject.Inject;

import java.lang.reflect.Field;


public class BaseController {

    @Inject
    protected TransactionalRepository transactionalRepository;

    @Inject
    protected FilesRepository filesRepository;

    @Inject
    protected ContractRepository contractRepository;

    @Inject
    protected CategoryDictionaryRepository categoryDictionaryRepository;

    @Inject
    protected AddressRepository addressRepository;

    @Inject
    protected EmailService emailService;

    @Inject
    protected SecurityService securityService;

    @Inject
    protected FilesService filesService;

    @Inject
    protected ResponseService responseService;

    @Inject
    protected UserRepository userRepository;
    @Inject
    protected UserLogoutService logoutService;


    @Value("${micronaut.router.folder.dir-pattern}")
    protected String dirPattern;
    @Value("${micronaut.router.folder.files.avatars}")
    protected String avatars;

    public Pageable getPageable(Integer pageNum, Integer pageSize) {
        return Pageable.from((pageNum != null) ? pageNum : 0, (pageSize != null) ? pageSize : Integer.MAX_VALUE);
    }

    public User getCurrentUser(){
        return userRepository.findById(
                ((CustomAuthentication)securityService.getAuthentication().orElseThrow())
                        .getUid()
        ).orElseThrow();
    }

    public String getUserId(){
        return ((CustomAuthentication)securityService.getAuthentication().orElseThrow(()-> new RuntimeException("Пользователь не найден")))
                .getUid();
    }
    /*public Optional<User> getCurrentUser(){
        return userRepository.findByUserNickName(String.valueOf(securityService.username()));
    }*/

    public MediaType fileTypeResolver(String filePath){
        if (filePath == null || filePath.equals("")) throw new RuntimeException("Invalid file name for type resolving");

        switch (filePath.substring(filePath.lastIndexOf(".")+1)){
            case "jpg" -> {
                return MediaType.IMAGE_JPEG_TYPE;
            }
            case "gif" -> {
                return MediaType.IMAGE_GIF_TYPE;
            }
            case "png" -> {
                return MediaType.IMAGE_PNG_TYPE;
            }
            default -> {
                return MediaType.APPLICATION_OCTET_STREAM_TYPE;
            }
        }
    }

    public static <T> T updateEntity(T entity, T update) throws IllegalAccessException {
        for (Field field : update.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(update) != null) {
                field.set(entity, field.get(update));
            }
        }
        return entity;
    }
}
