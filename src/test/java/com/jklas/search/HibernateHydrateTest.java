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
package com.jklas.search;


import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.PreDeleteEventListener;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEventListener;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jklas.search.engine.Search;
import com.jklas.search.engine.VectorSearch;
import com.jklas.search.engine.dto.ObjectResult;
import com.jklas.search.engine.dto.VectorRankedResult;
import com.jklas.search.exception.SearchEngineMappingException;
import com.jklas.search.index.IndexId;
import com.jklas.search.index.memory.MemoryIndex;
import com.jklas.search.index.memory.MemoryIndexReaderFactory;
import com.jklas.search.index.memory.MemoryIndexWriterFactory;
import com.jklas.search.indexer.DefaultIndexerService;
import com.jklas.search.indexer.pipeline.DefaultIndexingPipeline;
import com.jklas.search.interceptors.SearchInterceptor;
import com.jklas.search.interceptors.hibernate.HibernateEventInterceptor;
import com.jklas.search.query.vectorial.VectorQuery;
import com.jklas.search.query.vectorial.VectorQueryParser;
import com.jklas.search.templates.hibernate.HibernatePluginTemplate;
import com.jklas.search.util.Utils;
import com.kstore.entity.customer.Customer;
import com.kstore.entity.item.Item;
import com.kstore.entity.site.Site;

public class HibernateHydrateTest {

	private static SessionFactory sessionFactory;

	private Session session = null;

	@BeforeClass
	public static void setupSessionFactory() throws SearchEngineMappingException{
		Utils.configureAndMap(Customer.class);
		Utils.configureAndMap(Item.class);

		HibernateEventInterceptor listener= 
			new HibernateEventInterceptor(
					new SearchInterceptor(
							new DefaultIndexerService(
									new DefaultIndexingPipeline(),
									MemoryIndexWriterFactory.getInstance())));


		Configuration configuration = new Configuration().configure();
		configuration.getEventListeners().setPreInsertEventListeners(new PreInsertEventListener[]{listener});
		configuration.getEventListeners().setPreUpdateEventListeners(new PreUpdateEventListener[]{listener});
		configuration.getEventListeners().setPreDeleteEventListeners(new PreDeleteEventListener[]{listener});

		sessionFactory = configuration.buildSessionFactory();

	}

	@Before
	public void Setup(){
		session = sessionFactory.openSession();		
		session.beginTransaction();
		session.setFlushMode(FlushMode.ALWAYS);
	}

	@After
	public void Cleanup(){
		MemoryIndex.renewAllIndexes();
		session.getTransaction().rollback();
		session.getTransaction().begin();
	}

	@Test
	public void ObjectIsHydrated() {
		Site argentina = new Site("AR");
		session.save(argentina);
		Customer julian = new Customer(argentina,"Julian","Klas","jklas@fi.uba.ar","654321");
		session.save(julian);
		session.flush();

		VectorQuery query = new VectorQueryParser("julian").getQuery(new IndexId("AR"));
		List<VectorRankedResult> search = new VectorSearch(query, MemoryIndexReaderFactory.getInstance()).search();
		
		Assert.assertEquals(1, search.size());
		
		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());
		Assert.assertEquals(1, MemoryIndex.getIndex(new IndexId("AR")).getObjectCount());
		
		Object hydrated = session.load(search.get(0).getKey().getClazz(), search.get(0).getKey().getId());
		
		Assert.assertEquals(julian, hydrated);
	}
	
	@Test
	public void ObjectIsHydratedByTemplate() {
		Site argentina = new Site("AR");
		session.save(argentina);
		Customer julian = new Customer(argentina,"Julian","Klas","jklas@fi.uba.ar","654321");
		session.save(julian);
		session.flush();

		VectorQuery query = new VectorQueryParser("julian").getQuery(new IndexId("AR"));
		Search search = new VectorSearch(query, MemoryIndexReaderFactory.getInstance());
		
		Collection<? extends ObjectResult> searchResults = search.search();
		
		HibernatePluginTemplate template = new HibernatePluginTemplate();
		
		Collection<?> hydratedResults = template.hydrateAll(session, searchResults);
			
		
		Assert.assertEquals(1, hydratedResults.size());
		Assert.assertEquals(julian, hydratedResults.iterator().next());
	}

	@Test
	public void ObjectIsHydratedByGenericTemplate() {
		Site argentina = new Site("AR");
		session.save(argentina);
		Customer julian = new Customer(argentina,"Julian","Klas","jklas@fi.uba.ar","654321");
		session.save(julian);
		session.flush();

		VectorQuery query = new VectorQueryParser("julian").getQuery(new IndexId("AR"));
		Search search = new VectorSearch(query, MemoryIndexReaderFactory.getInstance());
		
		Collection<? extends ObjectResult> searchResults = search.search();
		
		HibernatePluginTemplate template = new HibernatePluginTemplate();
		
		Collection<Customer> hydratedResults = template.hydrateAll(session, searchResults, Customer.class);
					
		Assert.assertEquals(1, hydratedResults.size());
		Assert.assertEquals(julian, hydratedResults.iterator().next());
	}
	
}
