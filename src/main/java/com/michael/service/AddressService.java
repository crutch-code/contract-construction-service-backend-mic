package com.michael.service;

import com.michael.entity.jpa.Address;
import com.michael.repository.AddressRepository;
import com.michael.repository.TransactionalRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;

@Singleton
public class AddressService {

    @Inject
    TransactionalRepository transactionalRepository;

    @Inject
    AddressRepository addressRepository;


}
