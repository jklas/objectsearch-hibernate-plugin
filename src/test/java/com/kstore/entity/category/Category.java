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
package com.kstore.entity.category;

import java.util.Set;

import com.jklas.search.annotations.Indexable;
import com.jklas.search.annotations.IndexReference;
import com.jklas.search.annotations.SearchCollection;
import com.jklas.search.annotations.SearchField;
import com.jklas.search.annotations.SearchId;
import com.kstore.entity.site.Site;

@Indexable
public class Category {

	@SearchId
	private long id;
	
	private Site site;
	
	@SearchField
	private String name;
	
	private Category mirror;
	
	private Category parent;
	
	@SearchCollection(reference=IndexReference.SELF)
	private Set<Category> childs;

	private Integer level = 1;
	
	Category() {}	
	
	public Category(Site site, String name, Category parent, Set<Category> childs) {
		this.site = site;
		this.name = name;
		this.setParent(parent);
				
		if(childs!=null) {
			for (Category child : childs) {
				child.setParent(this);
			}			
		}
	}
	
	public Category(Site site, String name, Set<Category> childs) {
		this.site = site;
		this.name = name;
						
		for (Category child : childs) {
			child.setParent(this);
		}
	}
	
	public Category(Site site, String name, Category parent) {
		this.site = site;
		this.name = name;
		this.setParent(parent);
	}
	
	public Category(Site site, String name) {
		this.site = site;
		this.name = name;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Site getSite() {
		return site;
	}

	public String getName() {
		return name;
	}

	public void setMirror(Category mirror) {
		this.mirror = mirror;
	}

	public Category getMirror() {
		return mirror;
	}

	public void setParent(Category parent) {
		this.parent = parent;
		if(parent!=null) setLevel(parent.getLevel()+1);
	}

	public Category getParent() {
		return parent;
	}

	private void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getLevel() {
		return level;
	}

	public void setChilds(Set<Category> childs) {
		this.childs = childs;
	}

	public Set<Category> getChilds() {
		return childs;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setSite(Site site) {
		this.site = site;
	}
	
	@Override
	public String toString() {
		return 	"Site: " + getSite().toString() + "\n" +
				"ID: " + getId() + "\n" +
				"Name: " + getName() + "\n" +
				"Level: " + getLevel() + "\n" +
				"Parent: " + getParent().getId();
	}
}
