package com.kstore.search;

import java.util.HashMap;

import com.jklas.search.indexer.Transform;
import com.kstore.entity.site.Site;

public class CustomerSiteTransform extends Transform<Site> {

	private static HashMap<String,String> translationTable = new HashMap<String,String>();
	
	public CustomerSiteTransform() {
		translationTable.put("AR", "ARGENTINA");
	}
	
	@Override
	public String transform(Site e) {		
		if(translationTable.containsKey(e.getSiteID())) return translationTable.get(e.getSiteID());
		
		return e.getSiteID();
	}

}
