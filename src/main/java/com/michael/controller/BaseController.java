package com.michael.controller;


import com.michael.entity.User;
import com.michael.repository.FilesRepository;
import com.michael.repository.TransactionalRepository;
import com.michael.repository.UserRepository;
import com.michael.service.EmailService;
import com.michael.service.ErrorService;
import com.michael.service.FilesService;
import com.michael.service.UserLogoutService;
import com.michael.utills.security.CustomAuthentication;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.MediaType;
import io.micronaut.security.utils.SecurityService;
import jakarta.inject.Inject;


public class BaseController {

    @Inject
    protected TransactionalRepository transactionalRepository;

    @Inject
    protected FilesRepository filesRepository;

    @Inject
    protected EmailService emailService;

    @Inject
    protected SecurityService securityService;

    @Inject
    protected FilesService filesService;

    @Inject
    protected ErrorService errorService;

    @Inject
    protected UserRepository userRepository;
    @Inject
    protected UserLogoutService logoutService;


    @Value("${micronaut.router.folder.dir-pattern}")
    protected String dirPattern;
    @Value("${micronaut.router.folder.files.post-photos}")
    protected String postPhotos;
    @Value("${micronaut.router.folder.files.avatars}")
    protected String avatars;
    @Value("${micronaut.router.folder.files.documents}")
    protected String documents;
    @Value("${micronaut.router.folder.files.secure-pictures}")
    protected String securePhotos;

    public User getCurrentUser(){
        return userRepository.findById(
                ((CustomAuthentication)securityService.getAuthentication().orElseThrow())
                        .getUid()
        ).orElseThrow();
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
}
