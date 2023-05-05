package com.michael.repository;

import com.michael.entity.jpa.Message;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface MessageRepository extends CrudRepository<Message, String> {
}
