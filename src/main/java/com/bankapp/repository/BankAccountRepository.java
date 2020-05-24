package com.bankapp.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.model.BankAccount;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, String> {

	Optional<BankAccount> findByAccountNumber(String accountNumber);
	
	@Query(value = "SELECT max(id) FROM bankaccount",nativeQuery = true)
	BigDecimal findmaxId();
}
