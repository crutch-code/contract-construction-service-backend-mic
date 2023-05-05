package com.michael.entity.jpa;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.michael.entity.jsonviews.JsonViewsCollector;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "category_dictionary")
@JsonView(JsonViewsCollector.Entity.Default.class)
public class CategoryDictionary extends BaseEntity{

    @NotNull
    @Column(name = "category_master", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @JsonProperty("category_master")
    @Schema(name = "category_master")
    private String categoryMaster;

    @Column(name = "category_sub")
    @Type(type = "org.hibernate.type.TextType")
    @JsonProperty("category_sub")
    @Schema(name = "category_sub")
    private String categorySub;


    public CategoryDictionary(String id, String categoryMaster, String categorySub) {
        super(id);
        this.categoryMaster = categoryMaster;
        this.categorySub = categorySub;
    }
}