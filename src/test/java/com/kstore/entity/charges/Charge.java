package com.kstore.entity.charges;

import com.kstore.entity.customer.Customer;

public class Charge {
	
	private long id;
	
	private Customer customer;
	
	private float amount;

	private String reason;

	public Charge(long id, Customer customer) {
		this.id = id;
		this.customer = customer;
	}
	
	public long getId() {
		return id;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getAmount() {
		return amount;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
}
