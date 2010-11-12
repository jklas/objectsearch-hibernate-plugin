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
package com.jklas.search.interceptors.hibernate;

import java.io.Serializable;

import org.apache.commons.logging.LogFactory;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.jklas.search.exception.IndexObjectException;
import com.jklas.search.index.dto.IndexObjectDto;
import com.jklas.search.interceptors.SearchInterceptor;

/**
 * Interceptor de Hibernate
 * 
 * @author Julán
 * @since 1.0
 * @date 2009-08-31
 */
public class HibernateInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;

	private SearchInterceptor searchInterceptor;

	@SuppressWarnings("unused")
	private HibernateInterceptor() {}
	
	public HibernateInterceptor(SearchInterceptor searchInterceptor) {
		this.searchInterceptor = searchInterceptor;
	}

	public void setSearchInterceptor(SearchInterceptor searchInterceptor) {
		this.searchInterceptor = searchInterceptor;
	}
	
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {

		return onSave(entity, id, currentState, propertyNames, types);
	}
		
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		try {
			searchInterceptor.update(new IndexObjectDto(entity, id));
		} catch (IndexObjectException e) {
			LogFactory.getLog(getClass()).error("Error al indexar. Entidad: "+entity+" - id:"+id,e);
		}		

		return false;
	}

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

		try {
			searchInterceptor.delete(new IndexObjectDto(entity, id));
		} catch (IndexObjectException e) {
			LogFactory.getLog(getClass()).error("Error al indexar. Entidad: "+entity+" - id:"+id,e);
		}
	}

}
