package com.bankapp.model;

import java.util.List;

public class HeadOffice {

	private List<Branch> branches;
	
	public void createBranch(){
		//Actual Implementation is in BankingController
	}
	
	public void getBranchById(String branchId) {
		//Actual Implementation is in BankingController
	}
	
	public void getAllBranches() {
		//Actual Implementation is in BankingController
	}

	public List<Branch> getBranches() {
		return branches;
	}

	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}
}
