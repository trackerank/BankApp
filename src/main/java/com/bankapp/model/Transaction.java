package com.bankapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="transaction")
public class Transaction {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue
	private long id;
	
	@Column(name="transaction_id", unique = true, nullable = false)
	private String transactionId;
	
	@Column(name="amount")
	private Double amount;
	
	@Column(name="type")
	private String type;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
