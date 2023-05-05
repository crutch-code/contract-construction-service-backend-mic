package com.michael.repository;

import com.michael.entity.jpa.Address;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;

import javax.annotation.Nullable;

@Repository
public interface AddressRepository extends CrudRepository<Address, String> {

    @Query(
            value = "from Address t where " +
                    "t.country like concat('%', :country, '%') " +
                    "and t.city like concat('%', :city, '%') " +
                    "and t.street like concat('%', :street, '%') " +
                    "and (:build is null or t.apart =:build) " +
                    "and (:apart is null or t.apart =:apart) ",
            countQuery = "select count(t) from Address t where " +
                    "t.country like concat('%', :country, '%') " +
                    "and t.city like concat('%', :city, '%') " +
                    "and t.street like concat('%', :street, '%') " +
                    "and (:build is null or t.apart =:build) " +
                    "and (:apart is null or t.apart =:apart) "
    )
    Page<Address> getFiltered(
            String country,
            String city,
            String street,
            @Nullable Integer build,
            @Nullable Integer apart,
            Pageable pageable
    );
}
