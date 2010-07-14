package com.kstore.business.dto;

public class CustomerDto {

	private String site;
	
	private Long id;
	
	private String email;
	
	private String password;
	
	private String nickname;

	public void setSite(String site) {
		this.site = site;
	}

	public String getSite() {
		return site;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}
	
	
}
