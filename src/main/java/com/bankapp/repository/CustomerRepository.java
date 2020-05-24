package com.bankapp.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.model.Customer;


@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {

    Optional<Customer> findByPanNumber(String panNumber);
    
}
