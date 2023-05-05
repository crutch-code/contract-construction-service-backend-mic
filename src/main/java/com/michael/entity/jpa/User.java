package com.michael.entity.jpa;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.michael.entity.jsonviews.JsonViewsCollector;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.jackson.annotation.JacksonFeatures;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users", schema = "public")
@Introspected
@Getter
@Setter
@JsonView(JsonViewsCollector.Entity.Default.class)
@DynamicInsert
@JacksonFeatures(additionalModules = JavaTimeModule.class)
public class User extends BaseEntity {
    @JsonInclude
    @JsonProperty("user_name")
    @Column(name = "user_name")
    @JsonAlias("userName")
    private String userName;
    @JsonInclude
    @JsonProperty("user_birthday")
    @Column(name = "user_birthday")
    @JsonAlias("userBirthday")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate userBirthday;

    @JsonInclude
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("user_reg_date")
    @Column(name = "user_reg_date")
    @JsonAlias("userRegDate")
    private LocalDate userRegDate;

    @JsonInclude
    @JsonProperty("user_email")
    @Column(name = "user_email")
    @JsonAlias("userEmail")
    private String userEmail;


    @JsonProperty("user_password")
    @Column(name = "user_password")
    @JsonAlias("userPassword")
    @JsonView(JsonViewsCollector.Users.WithPassword.class)
    private String userPassword;

    @ManyToMany
    @JoinTable(name = "users_avatars",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id"))
    @JsonInclude
    @JsonView(JsonViewsCollector.Users.WithAvatars.class)
    private Set<Files> avatars = new java.util.LinkedHashSet<>();

    @JsonInclude
    @JsonProperty("user_phone")
    @Column(name = "user_phone")
    @JsonAlias("userPhoneNumber")
    private String userPhone;

    @JsonInclude
    @JsonProperty("user_is_confirm")
    @Column(name = "user_is_confirm", nullable = false, columnDefinition = "boolean default false")
    @JsonAlias("userIsConfirm")
    private Boolean isConfirm;


    @JsonInclude
    @JsonProperty("user_is_operator")
    @Column(name = "user_is_operator", nullable = false, columnDefinition = "boolean default false")
    @Schema(name = "user_is_operator")
    @JsonAlias("userIsOperator")
    private Boolean isOperator;

    @JsonInclude
    @JsonProperty("user_is_contractor")
    @Column(name = "user_is_contractor", nullable = false, columnDefinition = "boolean default false")
    @Schema(name = "user_is_contractor")
    @JsonAlias("userIsContractor")
    private Boolean isContractor;


    @JsonInclude
    @Column(name = "user_rating")
    private Double rating;

    public User(
            String id, String userName, LocalDate userBirthday, LocalDate userRegDate,
            String userEmail, String userPassword, Set<Files> avatars, String userPhone,
            Boolean isConfirm, Boolean isOperator, Boolean isContractor, Double rating
    ) {
        super(id);
        this.userName = userName;
        this.userBirthday = userBirthday;
        this.userRegDate = userRegDate;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.avatars = avatars;
        this.userPhone = userPhone;
        this.isConfirm = isConfirm;
        this.isOperator = isOperator;
        this.isContractor = isContractor;
        this.rating = rating;
    }

    public User() {

    }


}
