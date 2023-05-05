package com.michael.entity.jpa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.michael.entity.jpa.BaseEntity;
import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "files", schema = "public")
@Introspected
@Getter
@Setter
@NoArgsConstructor
public class Files extends BaseEntity {

    @JsonInclude
    @Column(name = "file_path")
    @JsonProperty(value = "file_path")
    public String filePath;

    @JsonInclude
    @Column(name = "file_size")
    @JsonProperty(value = "file_size")
    public Long fileSize;


    public Files(String id, String filePath, Long fileSize) {
        super(id);
        this.filePath = filePath;
        this.fileSize = fileSize;

    }

}
