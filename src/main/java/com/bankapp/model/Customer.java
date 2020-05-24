package com.bankapp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="customer")
public class Customer {
    
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue
	private long id;
	
	@ElementCollection
    private List<BankAccount> bankAccounts;
	
    @Column(name="panNumber", unique = true, nullable = false)
    private String panNumber;
    
	public List<BankAccount> getBankAccounts() {
		if(null == bankAccounts || bankAccounts.isEmpty()) {
			bankAccounts = new ArrayList<BankAccount>();
		}
		return bankAccounts;
	}
	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
 
	
}
