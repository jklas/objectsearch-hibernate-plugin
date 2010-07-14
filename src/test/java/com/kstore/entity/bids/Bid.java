package com.kstore.entity.bids;

import com.kstore.entity.customer.Customer;
import com.kstore.entity.item.Item;

public class Bid {

	private Long id;
	
	private Customer bidder;
	
	private Item bidItem;

	public long getId() {
		return id;
	}
	
	public Customer getBidder() {
		return bidder;
	}

	public Item getBidItem() {
		return bidItem;
	}
	
	
}
