package com.michael.service;

import com.michael.entity.jpa.Files;
import com.michael.repository.TransactionalRepository;
import com.michael.utills.security.MD5Util;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Singleton
public class FilesService {

    @Inject
    TransactionalRepository transactionalRepository;


    @Value("${micronaut.router.folder.dir-pattern}")
    protected String dirPattern;
    @Value("${micronaut.router.folder.files.avatars}")
    protected String avatars;


    public Files save(CompletedFileUpload file, String destination) throws IOException, NoSuchAlgorithmException {
        Path path = Path.of(destination + uniqueName(file.getFilename()));
        java.nio.file.Files.copy(file.getInputStream(), path);
        return new Files(
                transactionalRepository.genOid().orElseThrow(),
                path.toString().replace("\\", "/"),
                Long.parseLong(String.valueOf(file.getSize()))
        );
    }

    public String uniqueName(String old) throws NoSuchAlgorithmException {
        return MD5Util.getMD5(old + System.currentTimeMillis()) + old.substring(old.lastIndexOf("."));
    }

    @Async
    @EventListener
    public void initDirs(ApplicationStartupEvent e){
        Arrays.asList(
                new File(dirPattern),
                new File(dirPattern + avatars)
        ).forEach(File::mkdirs);
    }

    public String getDirPattern() {
        return dirPattern;
    }


    public String getAvatars() {
        return avatars;
    }

}
