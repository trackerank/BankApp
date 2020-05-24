package com.bankapp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


@Service
@Transactional
public class BankingServiceImpl implements BankingService {

	@Autowired
    private CustomerRepository customerRepository;
	@Autowired
    private BankAccountRepository bankAccountRepository;
	@Autowired
    private BranchRepository branchRepository;
	@Autowired
    private TransactionRepository transactionRepository;
	
    public BankingServiceImpl(CustomerRepository repository) {
        this.customerRepository=repository;
    }

	@Override
	public String createBranch() {
		Branch branch = new Branch();
		
		BigDecimal maxId = branchRepository.findmaxId();
		if(null!=maxId) {
			branch.setBranchId(String.valueOf(maxId.plus()));
		}else {
			//in setter method, it will automatically assign value starting from 1
			branch.setBranchId(null);
		}
		branchRepository.save(branch);
		return "Successfully created branch with branch Id="+branch.getBranchId();
	}

	@Override
	public Object getBranchById(String branchId) throws Exception {
		
		Optional<Branch> branchOp = branchRepository.findByBranchId(branchId);
		
		if(branchOp.isPresent()) {
			return branchOp.get();
		}else {
			return "No Branch Found";
		}
		
		
	}

	@Override
	public List<Branch> getAllBranches() {
		
		List<Branch> branchList = new ArrayList<Branch>();
		Iterable<Branch> branchItr = branchRepository.findAll();
		
		branchItr.forEach(branch -> {
			branchList.add(branch);
        });
		
		return branchList;
	}

	@Override
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
		//To get Auto incremented account number, setting null, in setter method the logic is defined for increment
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

	@Override
	public Object getAccountByAccountNumber(String accountNumber) throws Exception {
		Optional<BankAccount> accountOp = bankAccountRepository.findByAccountNumber(accountNumber);
		if(accountOp.isPresent()) {
			return accountOp.get();
		}else {
			return "No Account Found";
		}
	}
	
	@Override
	public Object getCustomerByPan(String panNumber) throws Exception {
		Optional<Customer> customerOp = customerRepository.findByPanNumber(panNumber);
		if(customerOp.isPresent()) {
			return customerOp.get();
		}else {
			return "No Customer Found";
		}
	}
	
	@Override
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
	
	@Override
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
