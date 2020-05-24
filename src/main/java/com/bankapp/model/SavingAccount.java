package com.bankapp.model;

import javax.persistence.Entity;

@Entity
public class SavingAccount extends BankAccount {

	public SavingAccount() {
		super();
		getMinimumBalance();
		getInterestRate();
		getType();
	}
	@Override
	public String getType() {
		return "Saving";
	}
	@Override
	public Double getMinimumBalance() {
		return 10000.0;
	}
	@Override
	public Double getInterestRate() {
		return 4.5;
	}

}
