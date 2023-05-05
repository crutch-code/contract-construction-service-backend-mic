package com.michael.entity.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "contractor_card")
@JsonView(JsonViewsCollector.Entity.Default.class)
public class ContractorCard extends BaseEntity{

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contractor_profile", nullable = false)
    @JsonProperty("contractor_profile")
    @Schema(name = "contractor_profile")
    private User contractorProfile;

    @Column(name = "contractor_company")
    @Type(type = "org.hibernate.type.TextType")
    @JsonProperty("contractor_company")
    @Schema(name = "contractor_company")
    private String contractorCompany;

    @NotNull
    @Column(name = "contractor_title", nullable = false)
    @JsonProperty("contractor_title")
    @Schema(name = "contractor_title")
    private String contractorTitle;

    @Column(name = "contractor_description")
    @Type(type = "org.hibernate.type.TextType")
    @JsonProperty("contractor_description")
    @Schema(name = "contractor_description")
    private String contractorDescription;

    @NotNull
    @Column(name = "contractor_act_date", nullable = false)
    @JsonProperty("contractor_act_date")
    @Schema(name = "contractor_act_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate contractorActDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contractor_card_address", nullable = false)
    @JsonProperty("contractor_card_address")
    @Schema(name = "contractor_card_address")
    private Address contractorCardAddress;


    @ManyToMany
    @JoinTable(name = "contractor_categories",
            joinColumns = @JoinColumn(name = "category_dictionary", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "contractor_card", referencedColumnName = "id"))
    @JsonProperty("category_dictionary")
    @Schema(name = "category_dictionary")
    private Set<CategoryDictionary> categoryDictionary;

    public ContractorCard(
            String id, User contractorProfile, String contractorCompany, String contractorTitle,
            String contractorDescription, LocalDate contractorActDate, Address contractorCardAddress,
            Set<CategoryDictionary> categoryDictionary
    ) {
        super(id);
        this.contractorProfile = contractorProfile;
        this.contractorCompany = contractorCompany;
        this.contractorTitle = contractorTitle;
        this.contractorDescription = contractorDescription;
        this.contractorActDate = contractorActDate;
        this.contractorCardAddress = contractorCardAddress;
        this.categoryDictionary = categoryDictionary;
    }
}