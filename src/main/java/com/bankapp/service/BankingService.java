package com.bankapp.service;

import java.util.List;

import com.bankapp.model.Branch;

public interface BankingService {

	public String createBranch();
	public Object getBranchById(String branchId) throws Exception;
	
	public List<Branch> getAllBranches();
	public Object getAccountByAccountNumber(String accountNumber) throws Exception;
	public Object getCustomerByPan(String panNumber) throws Exception;
	public String deposit(String accountNumber, Double amount);
	public String withdraw(String accountNumber, Double amount);
	public String createBankAccount(String panNumber, String type, Double amount, String branchId);
    
    
}
