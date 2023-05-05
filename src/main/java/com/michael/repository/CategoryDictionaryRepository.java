package com.michael.repository;

import com.michael.entity.jpa.CategoryDictionary;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface CategoryDictionaryRepository extends CrudRepository<CategoryDictionary, String> {

    public Page<CategoryDictionary> findAll(Pageable pageable);


    @Query(
            value = "select exists(" +
                        "select from category_dictionary as t " +
                        "where t.category_master like concat('%', :master ,'%') " +
                        "and t.category_sub like concat('%', :sub ,'%')" +
                    ")",
            nativeQuery = true
    )
    Boolean valid(String master, String sub);
}
