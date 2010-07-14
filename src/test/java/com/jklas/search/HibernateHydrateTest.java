package com.jklas.search;


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

import com.jklas.search.engine.VectorSearch;
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
}
