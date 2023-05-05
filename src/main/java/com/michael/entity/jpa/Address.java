package com.michael.entity.jpa;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.michael.entity.jsonviews.JsonViewsCollector;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Introspected
@NoArgsConstructor
@Table(name = "address")
@JsonView(JsonViewsCollector.Entity.class)
public class Address extends BaseEntity{

    @NotNull
    @Column(name = "country", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String country;

    @NotNull
    @Column(name = "city", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String city;

    @NotNull
    @Column(name = "street", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String street;

    @NotNull
    @Column(name = "build", nullable = false)
    private Integer build;

    @Column(name = "apart")
    private Integer apart;

    @Column(name = "coord_x")
    @JsonProperty("coord_x")
    @Schema(name = "coord_x")
    private BigDecimal coordX;

    @Column(name = "coord_y")
    @JsonProperty("coord_y")
    @Schema(name = "coord_y")
    private BigDecimal coordY;

    public Address(String id, String country, String city, String street, Integer build, Integer apart, BigDecimal coordX, BigDecimal coordY) {
        super(id);
        this.country = country;
        this.city = city;
        this.street = street;
        this.build = build;
        this.apart = apart;
        this.coordX = coordX;
        this.coordY = coordY;
    }
}