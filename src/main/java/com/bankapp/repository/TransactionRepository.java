package com.bankapp.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.model.Transaction;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String> {

    Optional<Transaction> findByTransactionId(String transactionId);
    
    @Query(value = "SELECT max(id) FROM transaction",nativeQuery = true)
	BigDecimal findmaxTransactionId();
}
