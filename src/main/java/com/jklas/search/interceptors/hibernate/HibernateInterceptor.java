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
