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
