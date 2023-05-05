package com.michael.repository;

import com.michael.entity.jpa.Chat;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ChatRepository extends CrudRepository<Chat, String> {
}
