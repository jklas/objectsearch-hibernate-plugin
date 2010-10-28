package com.jklas.search;


import java.util.Iterator;
import java.util.Map.Entry;

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

import com.jklas.search.exception.SearchEngineMappingException;
import com.jklas.search.index.IndexId;
import com.jklas.search.index.ObjectKey;
import com.jklas.search.index.PostingList;
import com.jklas.search.index.PostingMetadata;
import com.jklas.search.index.Term;
import com.jklas.search.index.memory.MemoryIndex;
import com.jklas.search.index.memory.MemoryIndexWriterFactory;
import com.jklas.search.indexer.DefaultIndexerService;
import com.jklas.search.indexer.pipeline.DefaultIndexingPipeline;
import com.jklas.search.interceptors.SearchInterceptor;
import com.jklas.search.interceptors.hibernate.HibernateEventInterceptor;
import com.jklas.search.util.Utils;
import com.kstore.entity.customer.Customer;
import com.kstore.entity.item.Item;
import com.kstore.entity.site.Site;

public class HibernateIndexTest {

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
	public void Setup() {
		session = sessionFactory.openSession();		
		session.beginTransaction();
		session.setFlushMode(FlushMode.ALWAYS);
	}

	@After
	public void Cleanup() {
		MemoryIndex.renewAllIndexes();
		session.getTransaction().rollback();
		session.getTransaction().begin();
	}

	@Test
	public void EntityIsIndexedWithSelectedIndex() {
		Site argentina = new Site("AR");

		session.save(argentina);

		Customer julian = new Customer(argentina,"Julián","Klas","jklas@fi.uba.ar","654321");

		session.save(julian);

		session.flush();

		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());
		Assert.assertEquals(1, MemoryIndex.getIndex(new IndexId("AR")).getObjectCount());
	}

	@Test
	public void InsertIsIdempotent() {
		Site argentina = new Site("AR");
		session.save(argentina);
		Customer julian = new Customer(argentina,"Julián","Klas","jklas@fi.uba.ar","654321");
		session.save(julian);
		session.save(julian);
		session.flush();

		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());
		Assert.assertEquals(1, MemoryIndex.getIndex(new IndexId("AR")).getObjectCount());
	}


	@Test
	public void EntitiesAreUpdated() throws SecurityException, NoSuchFieldException {
		Site argentina = new Site("AR");
		session.save(argentina);

		Customer julian = new Customer(argentina,"Julián","Klas","jklas@fi.uba.ar","654321");
		session.save(julian);

		session.flush();

		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());

		MemoryIndex index = MemoryIndex.getIndex(new IndexId("AR"));
		Assert.assertEquals(1, index.getObjectCount());
		PostingList postingList = index.getPostingList(new Term("KLAS"));
		Assert.assertEquals("jklas@fi.uba.ar", postingList.iterator().next().getValue().
				getStoredFieldValue(Customer.class.getDeclaredField("email")));

		julian = (Customer)session.get(Customer.class, julian.getId()); 

		julian.setEmail("jklas@otherserver.com");
		session.update(julian);

		session.flush();

		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());		
		index = MemoryIndex.getIndex(new IndexId("AR"));
		Assert.assertEquals(1, index.getObjectCount());
		postingList = index.getPostingList(new Term("KLAS"));
		Iterator<Entry<ObjectKey,PostingMetadata>> iterator = postingList.iterator();

		Assert.assertEquals("jklas@otherserver.com", iterator.next().getValue().
				getStoredFieldValue(Customer.class.getDeclaredField("email")));
	}

	@Test
	public void EntitiesDeleted() throws SecurityException, NoSuchFieldException {
		Site argentina = new Site("AR");
		session.save(argentina);

		Customer julian = new Customer(argentina,"Julián","Klas","jklas@fi.uba.ar","654321");
		session.save(julian);

		session.flush();

		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());

		MemoryIndex index = MemoryIndex.getIndex(new IndexId("AR"));
		Assert.assertEquals(1, index.getObjectCount());
		PostingList postingList = index.getPostingList(new Term("KLAS"));
		Assert.assertEquals("jklas@fi.uba.ar", postingList.iterator().next().getValue().
				getStoredFieldValue(Customer.class.getDeclaredField("email")));

		julian = (Customer)session.get(Customer.class, julian.getId()); 

		session.delete(julian);

		session.flush();

		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());		
		index = MemoryIndex.getIndex(new IndexId("AR"));
		Assert.assertEquals(0, index.getObjectCount());
	}
	
	@Test
	public void DeletesAreIdempotent() throws SecurityException, NoSuchFieldException {
		Site argentina = new Site("AR");
		session.save(argentina);

		Customer julian = new Customer(argentina,"Julián","Klas","jklas@fi.uba.ar","654321");
		session.save(julian);

		session.flush();

		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());

		MemoryIndex index = MemoryIndex.getIndex(new IndexId("AR"));
		Assert.assertEquals(1, index.getObjectCount());
		PostingList postingList = index.getPostingList(new Term("KLAS"));
		Assert.assertEquals("jklas@fi.uba.ar", postingList.iterator().next().getValue().
				getStoredFieldValue(Customer.class.getDeclaredField("email")));

		julian = (Customer)session.get(Customer.class, julian.getId()); 

		session.delete(julian);
		session.delete(julian);
		session.flush();

		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());		
		index = MemoryIndex.getIndex(new IndexId("AR"));
		Assert.assertEquals(0, index.getObjectCount());
	}

	@Test
	public void DeleteErasesInsert() {
		Site argentina = new Site("AR");
		session.save(argentina);
		Customer julian = new Customer(argentina,"Julián","Klas","jklas@fi.uba.ar","654321");
		session.save(julian);
		session.flush();
		
		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());
		Assert.assertEquals(1, MemoryIndex.getIndex(new IndexId("AR")).getObjectCount());
		
		session.delete(julian);
		session.flush();

		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());
		Assert.assertEquals(0, MemoryIndex.getIndex(new IndexId("AR")).getObjectCount());
	}

	@Test
	public void InsertUpdateDeleteFlow() {
		Site argentina = new Site("AR");
		session.save(argentina);
		Customer julian = new Customer(argentina,"Julián","Klas","jklas@fi.uba.ar","654321");
		session.save(julian);
		session.flush();
		
		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());
		Assert.assertEquals(1, MemoryIndex.getIndex(new IndexId("AR")).getObjectCount());
		
		julian.setEmail("jklas@otherserver.com");
		session.update(julian);
		session.flush();
		
		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());
		Assert.assertEquals(1, MemoryIndex.getIndex(new IndexId("AR")).getObjectCount());		
		
		session.delete(julian);
		session.flush();

		Assert.assertEquals(0, MemoryIndex.getDefaultIndex().getObjectCount());
		Assert.assertEquals(0, MemoryIndex.getIndex(new IndexId("AR")).getObjectCount());
	}
}
