package com.kstore.entity.item;

import java.util.Set;

import com.jklas.search.annotations.Indexable;
import com.jklas.search.annotations.LangId;
import com.jklas.search.annotations.SearchField;
import com.jklas.search.annotations.SearchId;
import com.jklas.search.annotations.Stemming;
import com.jklas.search.engine.stemming.IdentityStemmerStrategy;
import com.jklas.search.engine.stemming.StemType;
import com.kstore.entity.bids.Bid;
import com.kstore.entity.category.Category;
import com.kstore.entity.charges.Charge;
import com.kstore.entity.customer.Customer;
import com.kstore.entity.site.Site;

@Indexable @LangId(value="spanish")
public class Item {

	@SearchId
	private long id;

	private Site site;
	
	private double price;
	
	@SearchField
	@Stemming(strategy=IdentityStemmerStrategy.class,stemType=StemType.FULL_STEM)
	private String title;

	@SearchField
	private String description;
	
	private Customer seller;
	
	private Set<Bid> bids;
	
	private Set<Charge> charges;
	
	private String sellType;
	
	private Category category;
	
	// Hibernate
	Item() {}
	
	public Item(Site site, Customer seller, String sellType, 
			Category category, String title, String description, double price) {
		setSite(site);
		setSeller(seller);
		setSellType(sellType);
		setCategory(category);
		setTitle(title);
		setDescription(description);
		setPrice(price);
	}
	
	void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setSeller(Customer seller) {
		this.seller = seller;
	}

	public Customer getSeller() {
		return seller;
	}

	public void addBid(Bid bid) {
		this.bids.add(bid);
	}

	public Set<Bid> getBids() {
		return bids;
	}

	public void setCharges(Set<Charge> charges) {
		this.charges = charges;
	}

	public Set<Charge> getCharges() {
		return charges;
	}

	public void setSellType(String sellType) {
		this.sellType = sellType;
	}

	public String getSellType() {
		return sellType;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Site getSite() {
		return site;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return price;
	}	
	
	@Override
	public String toString() {
		return "Item ID: "+ getId() + "\n" +
		"Title: "+ getTitle()+ "\n" +
		( getDescription().length()>64 ?
				"Description: '"+ getDescription().substring(0, 64) + "..'\n" :
				"Description: '"+ getDescription() + "'\n")
		 +
		"Seller ID: "+ getSeller().getId();
	}
}
