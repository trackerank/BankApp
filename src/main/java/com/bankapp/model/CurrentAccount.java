package com.bankapp.model;

import javax.persistence.Entity;

@Entity
public class CurrentAccount extends BankAccount {

	public CurrentAccount() {
		super();
		getMinimumBalance();
		getInterestRate();
		getType();
	}

	@Override
	public String getType() {
		return "Current";
	}
	public Double getMinimumBalance() {
		return 20000.0;
	}
	@Override
	public Double getInterestRate() {
		return 0.0;
	}
}
