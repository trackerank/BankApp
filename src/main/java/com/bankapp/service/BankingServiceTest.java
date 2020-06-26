package com.bankapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankapp.model.BankAccount;
import com.bankapp.model.Branch;
import com.bankapp.model.CurrentAccount;
import com.bankapp.model.Customer;
import com.bankapp.model.SavingAccount;
import com.bankapp.model.Transaction;
import com.bankapp.repository.BankAccountRepository;
import com.bankapp.repository.BranchRepository;
import com.bankapp.repository.CustomerRepository;
import com.bankapp.repository.TransactionRepository;


@ExtendWith(MockitoExtension.class)
public class BankingServiceTest {

	@Mock
    private CustomerRepository customerRepository;
	@Mock
    private BankAccountRepository bankAccountRepository;
	@Mock
    private BranchRepository branchRepository;
	@Mock
    private TransactionRepository transactionRepository;
	
    
	@Test
	public void createBranch() {
		Branch branch = new Branch();
		
		BigDecimal maxId = new BigDecimal(101);
		if(null!=maxId) {
			branch.setBranchId(String.valueOf(maxId.plus()));
		}
		when(branchRepository.save(any(Branch.class))).thenReturn(new Branch());
		
		//return "Successfully created branch with branch Id="+branch.getBranchId();
		
		assertThat(branchRepository.save(branch)).isNotNull();
	}

	@Test
	public Object getBranchById(String branchId) throws Exception {
		
		Optional<Branch> branchOp = branchRepository.findByBranchId(branchId);
		
		if(branchOp.isPresent()) {
			return branchOp.get();
		}else {
			return "No Branch Found";
		}
		
		
	}

	@Test
	public List<Branch> getAllBranches() {
		
		List<Branch> branchList = new ArrayList<Branch>();
		Iterable<Branch> branchItr = branchRepository.findAll();
		
		branchItr.forEach(branch -> {
			branchList.add(branch);
        });
		
		return branchList;
	}

	@Test
	public String createBankAccount(String panNumber, String type, Double amount,String branchId) {
		
		Branch branch = null;
		//Check if branch exists, if no branch exists with branch id, account cannot be created 
		Optional<Branch> branchOp = branchRepository.findByBranchId(branchId);
		if(branchOp.isPresent()) {
			branch = branchOp.get();
		}else {
			return "Bank Account cannot be created as branch details could not be extracted from branchId";
		}
		
		Optional<Customer> customerOp = customerRepository.findByPanNumber(panNumber);
		
		Customer customer = null;
		if(customerOp.isPresent()) {
			customer = customerOp.get();
		}else {
			customer = new Customer();
			//For new user, pan number needs to be saved in Customer table
			customer.setPanNumber(panNumber);
		}
		BankAccount bankAccountToSave;
		BankAccount bankAccount= null;
		if("Current".equalsIgnoreCase(type)) {
			if(amount<20000) {
				return "Current Account can only be created with minimum balance of Rs 20000";
			}
			bankAccount = new CurrentAccount();
		}else {
			if(amount<10000) {
				return "Saving Account can only be created with minimum balance of Rs 10000";
			}
			bankAccount = new SavingAccount();
		}
		
		bankAccountToSave = bankAccount;
		BigDecimal maxId = bankAccountRepository.findmaxId();
		if(null!=maxId) {
			bankAccountToSave.setAccountNumber(String.valueOf(maxId.plus()));
		}else {
			//in setter method, it will automatically assign value starting from 101
			bankAccountToSave.setAccountNumber(null);
		}
		
		//Add account details to save in Database
		bankAccountToSave.setCurrentBalance(amount);
		bankAccountToSave.setMinimumBalance(bankAccount.getMinimumBalance());
		bankAccountToSave.setInterestRate(bankAccount.getInterestRate());
		bankAccountToSave.setType(bankAccount.getType());
		bankAccountRepository.save(bankAccountToSave);
		customer.getBankAccounts().add(bankAccountToSave);
		customerRepository.save(customer);
		
		//Add account and customer details to branch and save branch in DB
		branch.getBankAccounts().add(bankAccountToSave);
		branch.getCustomers().add(customer);
		branchRepository.save(branch);
		
		return "Successfully created Bank account with account number="+bankAccount.getAccountNumber();
	}

	@Test
	public Object getAccountByAccountNumber(String accountNumber) throws Exception {
		Optional<BankAccount> accountOp = bankAccountRepository.findByAccountNumber(accountNumber);
		if(accountOp.isPresent()) {
			return accountOp.get();
		}else {
			return "No Account Found";
		}
	}
	
	@Test
	public Object getCustomerByPan(String panNumber) throws Exception {
		Optional<Customer> customerOp = customerRepository.findByPanNumber(panNumber);
		if(customerOp.isPresent()) {
			return customerOp.get();
		}else {
			return "No Customer Found";
		}
	}
	
	@Test
	public String deposit(String accountNumber,Double amount) {
		
		if(amount<0) {
			return "Deposit Amount cannot be negative";
		}
		Optional<BankAccount> accountOp = bankAccountRepository.findByAccountNumber(accountNumber);
		BankAccount bankAccount = null;
		if(accountOp.isPresent()) {
			bankAccount = accountOp.get();
		}else {
			return "No Account Found For Deposit";
		}
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setType("deposit");
		BigDecimal maxTransactionId = transactionRepository.findmaxTransactionId();
		if(null != maxTransactionId) {
			transaction.setTransactionId(String.valueOf(maxTransactionId.plus()));
		}else {
			transaction.setTransactionId("1");
		}	
		transactionRepository.save(transaction);
		bankAccount.getTransactions().add(transaction);
		bankAccount.deposit(amount);
		return "Successfully deposited amount to accountNumber="+accountNumber;
	}
	
	@Test
	public String withdraw(String accountNumber,Double amount) {
		
		if(amount<0) {
			return "Withdraw Amount cannot be negative";
		}
		Optional<BankAccount> accountOp = bankAccountRepository.findByAccountNumber(accountNumber);
		BankAccount bankAccount = null;
		if(accountOp.isPresent()) {
			bankAccount = accountOp.get();
		}else {
			return "No Account Found For Withdraw";
		}
		if((bankAccount.getCurrentBalance() - bankAccount.getMinimumBalance()) >= amount) {
			bankAccount.withdraw(amount);
		}else {
			return "Withdrawal Amount cannot be processed";
		}
		
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setType("withdraw");
		BigDecimal maxTransactionId = transactionRepository.findmaxTransactionId();
		if(null != maxTransactionId) {
			transaction.setTransactionId(String.valueOf(maxTransactionId.plus()));
		}else {
			transaction.setTransactionId("1");
		}
		transactionRepository.save(transaction);
		bankAccount.getTransactions().add(transaction);
		bankAccountRepository.save(bankAccount);
		return "Successfully withdraw amount from accountNumber="+accountNumber;
	}
	
}
