package com.michael.entity.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.michael.entity.jsonviews.JsonViewsCollector;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "contract")
@NoArgsConstructor
@Introspected
@JsonView(JsonViewsCollector.Entity.Default.class)
public class Contract extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_category")
    @JsonProperty("contract_category")
    @Schema(name = "contract_category")
    private CategoryDictionary contractCategory;

    @NotNull
    @Column(name = "contract_price", nullable = false)
    @JsonProperty("contract_price")
    @Schema(name = "contract_price")
    private BigDecimal contractPrice;

    @NotNull
    @Column(name = "contract_created_date", nullable = false)
    @JsonProperty("contract_created_date")
    @Schema(name = "contract_created_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate contractCreatedDate;

    @Size(max = 30)
    @NotNull
    @Column(name = "contract_title", nullable = false, length = 30)
    @JsonProperty("contract_title")
    @Schema(name = "contract_title")
    private String contractTitle;

    @Column(name = "contract_description")
    @JsonProperty("contract_description")
    @Schema(name = "contract_description")
    private String contractDescription;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contract_user_creator", nullable = false)
    @JsonProperty("contract_user_creator")
    @Schema(name = "contract_user_creator")
    private User contractUserCreator;

    @NotNull
    @Column(name = "contract_status", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @JsonProperty("contract_status")
    @Schema(name = "contract_status")
    private String contractStatus;

    @Column(name = "contract_up_to_date")
    @JsonProperty("contract_up_to_date")
    @Schema(name = "contract_up_to_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate contractUpToDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contract_address", nullable = false)
    @JsonProperty("contract_address")
    @Schema(name = "contract_address")
    private Address contractAddress;

    @ManyToMany
    @JoinTable(name = "contract_files",
            joinColumns = @JoinColumn(name = "contract_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id"))
    @JsonInclude
    @JsonProperty("contract_files")
    @Schema(name = "contract_files")
    private Set<Files> contractFiles;

    public Contract(
            String id, CategoryDictionary contractCategory, BigDecimal contractPrice,
            LocalDate contractCreatedDate, String contractTitle, String contractDescription,
            User contractUserCreator, String contractStatus, LocalDate contractUpToDate,
            Address contractAddress, Set<Files> contractFiles
    ) {
        super(id);
        this.contractCategory = contractCategory;
        this.contractPrice = contractPrice;
        this.contractCreatedDate = contractCreatedDate;
        this.contractTitle = contractTitle;
        this.contractDescription = contractDescription;
        this.contractUserCreator = contractUserCreator;
        this.contractStatus = contractStatus;
        this.contractUpToDate = contractUpToDate;
        this.contractAddress = contractAddress;
    }
}