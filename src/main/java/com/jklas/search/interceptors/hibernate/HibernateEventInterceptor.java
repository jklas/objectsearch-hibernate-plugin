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

import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.event.PreDeleteEvent;
import org.hibernate.event.PreDeleteEventListener;
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;

import com.jklas.search.exception.IndexObjectException;
import com.jklas.search.index.dto.IndexObjectDto;
import com.jklas.search.interceptors.SearchInterceptor;


public class HibernateEventInterceptor
	implements PostInsertEventListener, PostDeleteEventListener, PostUpdateEventListener,
	PreInsertEventListener, PreDeleteEventListener, PreUpdateEventListener {
	
	private static final long serialVersionUID = 2044457057169984402L;
	
	private SearchInterceptor searchInterceptor;
	
	public HibernateEventInterceptor(SearchInterceptor searchInterceptor) {
		this.searchInterceptor = searchInterceptor;
	}
	
	@Override
	public void onPostInsert(PostInsertEvent event) {
		try {
			searchInterceptor.create(new IndexObjectDto(event.getEntity(), event.getId()));
		} catch (IndexObjectException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		try {
			searchInterceptor.delete(new IndexObjectDto(event.getEntity(), event.getId()));
		} catch (IndexObjectException e) {
			throw new RuntimeException(e);			
		}
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		try {
			searchInterceptor.update(new IndexObjectDto(event.getEntity(), event.getId()));
		} catch (IndexObjectException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean onPreInsert(PreInsertEvent event) {		
		try {
			searchInterceptor.create(new IndexObjectDto(event.getEntity(), event.getId()));
		} catch (IndexObjectException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	@Override
	public boolean onPreDelete(PreDeleteEvent event) {
		try {
			searchInterceptor.delete(new IndexObjectDto(event.getEntity(), event.getId()));
		} catch (IndexObjectException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		try {
			searchInterceptor.update(new IndexObjectDto(event.getEntity(), event.getId()));
		} catch (IndexObjectException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

}
