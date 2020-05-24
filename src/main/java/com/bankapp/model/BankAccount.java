package com.bankapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="bankaccount")
public abstract class BankAccount {
	
	private static AtomicInteger atomicInteger = new AtomicInteger(101);
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue
	private long id;
	
	@Column(name="account_number", unique = true, nullable = false)
	private String accountNumber;
	
	//@Column(name="accountNumber")
	private Double minimumBalance;
	@Column(name="account_type")
	private String type;
	@Column(name="current_balance")
	private Double currentBalance;
	@Column(name="interest_rate")
	private Double interestRate;
	
	//@OneToMany(mappedBy="transaction")
	@ElementCollection
	private List<Transaction> transactions;
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		if(null == accountNumber) {
			accountNumber = String.valueOf(atomicInteger.getAndIncrement());
		}
		this.accountNumber = accountNumber;
	}
	public Double getMinimumBalance() {
		return minimumBalance;
	}
	public void setMinimumBalance(Double minimumBalance) {
		this.minimumBalance = minimumBalance;
	}
	public Double getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}
	public Double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	public List<Transaction> getTransactions() {
		if(null == transactions || transactions.isEmpty()) {
			transactions = new ArrayList<Transaction>();
		}
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	public List<Transaction> getTransactionHistory() {
		return transactions;
	}
	public List<Transaction> getMiniStatement() {
		return transactions;
	}
	public void deposit(double amount) {
		this.currentBalance = this.currentBalance + amount;
	}
	public void withdraw(double amount) {
		this.currentBalance = this.currentBalance - amount;
	}
	public void setType(String type) {
		this.type = type;
	}
	public abstract String getType();
   
}
