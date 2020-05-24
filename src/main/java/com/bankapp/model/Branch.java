package com.bankapp.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


@Entity
@Table(name="branch")
public class Branch {
	
	private static AtomicInteger atomicInteger = new AtomicInteger(1);
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator",
             strategy = "enhanced-sequence",
             parameters = {
                 @Parameter(name = "initial_value",value = "1")
             })
	private long id;
	@Column(name="branch_id", unique = true, nullable = false)
	private String branchId;
	@ElementCollection
	private List<BankAccount> bankAccounts;
	@ElementCollection
	private List<Customer> customers;
	
	
	public void createBankAccount(String panNumber, String type, Double amount) {
		
	}
	public void getCustomerByPan(String panNumber) {
		
	}
	public void getAccountByAccountNumber(String accountNumber) {
		
	}
	
	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}
	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}
	public List<Customer> getCustomers() {
		return customers;
	}
	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		if(null == branchId) {
			branchId = String.valueOf(atomicInteger.getAndIncrement());
		}
		this.branchId = branchId;
	}
	
}
