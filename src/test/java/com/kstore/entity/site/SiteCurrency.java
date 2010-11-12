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
