package com.michael.service;

import com.michael.entity.jpa.Contract;
import com.michael.repository.CategoryDictionaryRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ContractService {

    @Inject
    CategoryDictionaryRepository categoryDictionaryRepository;

    @Inject
    ContractService contractService;



}
