package com.kstore.business.servicelayer;

import com.kstore.entity.customer.Customer;
import com.kstore.entity.item.Item;

public interface ItemService {
	public boolean publish(Customer customer, Item item);
}
