package com.michael.entity.jpa;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.michael.entity.jsonviews.JsonViewsCollector;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@MappedSuperclass
@Getter
@Setter
@JsonView(JsonViewsCollector.BaseEntity.class)
public class BaseEntity {
    @Id
    @JsonInclude
    @Column(name= "id")
    @JsonView({JsonViewsCollector.BaseEntity.OnlyOid.class})
    public String id;

    public BaseEntity(String id) {
        this.id = id;
    }

    public BaseEntity() {
    }
}
