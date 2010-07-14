package com.kstore.entity.charges;

import java.util.Set;

import com.kstore.entity.customer.Customer;

public class Bill {

	private Long id;
	
	private Set<Charge> charges;
	
	private Customer customer;
	
	Bill() {}
	
	public Long getId() {
		return id;
	}
	
	public Set<Charge> getCharges() {
		return charges;
	}
	
	public void setCharges(Set<Charge> charges) {
		this.charges = charges;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
}
