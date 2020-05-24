package com.bankapp.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.model.Branch;


@Repository
public interface BranchRepository extends CrudRepository<Branch, String> {

    Optional<Branch> findByBranchId(String branchId);
    
    @Query(value = "SELECT max(id) FROM branch",nativeQuery = true)
	BigDecimal findmaxId();

}
