package com.michael.repository;

import com.michael.entity.jpa.User;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, String> {

     Optional<User> findByUserEmail(String email);
     Optional<User> findByUserPhone(String phone);
     @Query(value = "update User as t set t.userIsConfirm=:confirm where t.id=:id")
     void updateUserIsConfirmById(String id, Boolean confirm);
     @Query(value = "update User as t set t.userPhone=:phone where t.id=:id")
     void updateUserPhoneNumberById(String phone, String id);

     @Query(value = "update User as t set t.userPassword=:password where t.id=:id")
     void updateUserPasswordById(String password, String id);

     @Query(
             value = "select exists(select from users t " +
                     "where t.user_email=:credential " +
                     "or t.user_phone =:credential)",
             nativeQuery = true
     )
     Boolean existsByUserEmailOrUserPhone(String credential);
}
