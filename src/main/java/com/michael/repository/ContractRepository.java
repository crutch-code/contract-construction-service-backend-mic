package com.michael.repository;

import com.michael.entity.jpa.Contract;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.data.repository.CrudRepository;

import javax.annotation.Nullable;
import java.util.List;

@Repository
public interface ContractRepository extends CrudRepository<Contract, String> {



    @Query(
            value = "from Contract as t " +
                    "where t.contractAddress.city like concat('%', :city, '%') " +
                    "and t.contractAddress.country like concat('%', :country, '%') " +
                    "and t.contractStatus like concat('%', :status, '%') "+
                    "and (:uid is null or t.contractUserCreator.id =:uid) " +
                    "and t.contractCategory.categoryMaster like concat('%', :category, '%') " +
                    "and t.contractCategory.categorySub like concat('%', :subCategory, '%') ",
            countQuery = "select count(t) from Contract as t " +
                    "where t.contractAddress.city like concat('%', :city, '%') " +
                    "and t.contractAddress.country like concat('%', :country, '%') " +
                    "and t.contractStatus like concat('%', :status, '%') "+
                    "and (:uid is null or t.contractUserCreator.id =:uid) " +
                    "and t.contractCategory.categoryMaster like concat('%', :category, '%') " +
                    "and t.contractCategory.categorySub like concat('%', :subCategory, '%') "

    )
    Page<Contract> getFiltered(
            String city,
            String country,
            String category,
            String subCategory,
            @Nullable String status,
            @Nullable String uid,
            Sort sort,
            Pageable pageable
    );

}
