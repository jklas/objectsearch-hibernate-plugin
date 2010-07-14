package com.kstore.entity.customer;

import com.jklas.search.annotations.IndexSelector;
import com.jklas.search.annotations.Indexable;
import com.jklas.search.annotations.SearchField;
import com.jklas.search.annotations.SearchFilter;
import com.jklas.search.annotations.SearchId;
import com.kstore.entity.site.Site;

@Indexable
public class Customer{

	@SearchId
	private Long id;
	
	@IndexSelector @SearchField
	private Site site;
	
	@SearchField	
	private String firstName;
	
	@SearchField
	private String lastName;
	
	@SearchFilter
	private String email;
	
	private String password;
	
	Customer() {}
	
	public Customer(Site site, String firstName, String lastName, String email, String password) {
		setSite(site);
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setPassword(password);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "Customer: "+getId()+"\n"+
			   "First Name: " + getFirstName() + "\n" + 
			   "Last Name: " + getLastName() + "\n" +
			   "Email: " + getEmail();
		
	}	
}
