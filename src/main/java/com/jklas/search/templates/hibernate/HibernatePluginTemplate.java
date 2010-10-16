package com.jklas.search.templates.hibernate;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Session;

import com.jklas.search.engine.dto.ObjectResult;

public class HibernatePluginTemplate {
	
	public Collection<?> hydrateAll(Session session, Collection<? extends ObjectResult> results) {
		
		ArrayList<Object> returnValues = new ArrayList<Object>(results.size());
		
		for (ObjectResult objectResult : results) {
			Object loaded = session.load(objectResult.getKey().getClazz(), objectResult.getKey().getId());
			returnValues.add(loaded);
		}
		
		return returnValues;		
	}

	public <T> Collection<T> hydrateAll(Session session, Collection<? extends ObjectResult> results, Class<T> desiredClass) {
		
		ArrayList<T> returnValues = new ArrayList<T>(results.size());
		
		for (ObjectResult objectResult : results) {
			Object loaded = session.load(objectResult.getKey().getClazz(), objectResult.getKey().getId());
			
			if(!desiredClass.isAssignableFrom( loaded.getClass() )) continue;
			else {	
				@SuppressWarnings("unchecked")
				T t = (T)loaded;
				returnValues.add(t);
			}			
		}
		
		return returnValues;		
	}
	
}
