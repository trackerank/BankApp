package com.bankapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.service.BankingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = { "Banking REST endpoints" })
@RequestMapping("/ctrl")
public class BankingController {

	@Autowired
	private BankingService bankingService;
	
	@PostMapping(path = "/branch/create")
	@ApiOperation(value = "Create a new Branch", notes = "Create a new branch initated by Head office")
	public ResponseEntity<Object> createBranch(){
		return ResponseEntity.ok(bankingService.createBranch());
	}
	
	@GetMapping(path = "/branch/{branchId}")
	@ApiOperation(value = "Find Branch", notes = "Find an existing branch by branch id")
	public ResponseEntity<Object> getBranchById(@PathVariable String branchId) throws Exception {
		return ResponseEntity.ok(bankingService.getBranchById(branchId));
	}
	
	@GetMapping(path = "/branch/getAll")
	@ApiOperation(value = "Get All Branches", notes = "Get details of all existing Branches")
	public ResponseEntity<Object> getAllBranches() {
		return ResponseEntity.ok(bankingService.getAllBranches());
	}
	
	@PostMapping(path = "/account/create")
	@ApiOperation(value = "Create a new account", notes = "Create a new account for existing/new customer")
	public ResponseEntity<Object> createBankAccount(
			@RequestParam("pan") String panNumber, 
			@RequestParam("type") String type, 
			@RequestParam("amount") Double amount,
			@RequestParam("branchId") String branchId){
		return ResponseEntity.ok(bankingService.createBankAccount(panNumber,type,amount,branchId));
		
	}
	
	@GetMapping(path = "/account/{accountNumber}")
	@ApiOperation(value = "Get account details", notes = "Get account details by account number")
	public ResponseEntity<Object> getAccountByAccountNumber(@PathVariable String accountNumber) throws Exception {
		return ResponseEntity.ok(bankingService.getAccountByAccountNumber(accountNumber));
	}
	
	@GetMapping(path = "/customer/{panNumber}")
	@ApiOperation(value = "Get Customer details", notes = "Get customer details by pan number")
	public ResponseEntity<Object> getCustomerByPan(@PathVariable String panNumber) throws Exception {
		return ResponseEntity.ok(bankingService.getCustomerByPan(panNumber));
	}
	
	@PostMapping(path = "/account/deposit")
	@ApiOperation(value = "Deposit into account", notes = "Deposit amount into an account")
	public ResponseEntity<Object> deposit(
			@RequestParam("accountNumber") String accountNumber,
			@RequestParam("amount") Double amount){
		return ResponseEntity.ok(bankingService.deposit(accountNumber,amount));
	}
	
	@PostMapping(path = "/account/withdraw")
	@ApiOperation(value = "Withdraw from account", notes = "Withdraw amount from an account")
	public ResponseEntity<Object> withdraw(
			@RequestParam("accountNumber") String accountNumber,
			@RequestParam("amount") Double amount){
		return ResponseEntity.ok(bankingService.withdraw(accountNumber,amount));
	}
}
