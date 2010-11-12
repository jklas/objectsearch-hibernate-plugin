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
