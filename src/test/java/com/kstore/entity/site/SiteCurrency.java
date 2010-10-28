package com.kstore.entity.site;

public class SiteCurrency {
	
	private Long id;
	
	private Site site; 
	
	private String shortName;
	
	private float conversionValue;
	
	SiteCurrency() {}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Site getSite() {
		return site;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setConversionValue(float conversionValue) {
		this.conversionValue = conversionValue;
	}

	public float getConversionValue() {
		return conversionValue;
	}
	
}
