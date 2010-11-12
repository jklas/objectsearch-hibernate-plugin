/**
 * Object Search Framework
 *
 * Copyright (C) 2010 Julian Klas
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
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
