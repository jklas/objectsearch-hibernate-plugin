package com.jklas.search;

import java.io.Serializable;
import java.util.Arrays;

import org.junit.Assert;

import com.jklas.search.annotations.Indexable;
import com.jklas.search.annotations.SearchField;
import com.jklas.search.annotations.SearchFilter;
import com.jklas.search.annotations.SearchId;
import com.jklas.search.configuration.AnnotationConfigurationMapper;
import com.jklas.search.exception.IndexObjectException;
import com.jklas.search.exception.SearchEngineMappingException;
import com.jklas.search.index.memory.MemoryIndex;
import com.jklas.search.index.memory.MemoryIndexWriterFactory;
import com.jklas.search.indexer.DefaultIndexerService;
import com.jklas.search.indexer.pipeline.DefaultIndexingPipeline;
import com.jklas.search.util.SearchLibrary;

public class Utils {

	public static void setupSampleMemoryIndex(Object... entities) {
		MemoryIndex.newDefaultIndex();
		for (int i = 0; i < entities.length; i++) {
			try {
				AnnotationConfigurationMapper.configureAndMap(entities[i], true);
			} catch (SearchEngineMappingException e) {
				Assert.fail();
				throw new RuntimeException("this shouldn't happened.. mapping failed!",e);
			}

			DefaultIndexerService dis = new DefaultIndexerService(
					new DefaultIndexingPipeline(),
					MemoryIndexWriterFactory.getInstance());

			try {
				dis.bulkCreate(Arrays.asList(entities[i]));				
			} catch (IndexObjectException e) {
				Assert.fail();
				throw new RuntimeException("this shouldn't happened.. can't construct IndexObjectDto",e);
			}

		}
	}
	
	public static void configureAndMap(Object entity) throws SearchEngineMappingException {
		SearchLibrary.configureAndMap(entity);	
	}
	
	public static void configureAndMap(Class<?> clazz) throws SearchEngineMappingException {
		SearchLibrary.configureAndMap(clazz);
	}
	
	@Indexable
	public static class SingleAttributeEntity implements Serializable {
		private static final long serialVersionUID = 5670740052272852510L;
		
		@SearchId public final int id;
		@SearchField public final String attribute;
		public SingleAttributeEntity(int id, String attribute) {
			this.id = id;
			this.attribute = attribute;
		}
	}
	
	@Indexable
	public static class DoubleAttributeEntity implements Serializable {
	
		private static final long serialVersionUID = -4005169601782127006L;
		
		@SearchId public final int id;
		@SearchField public final String a1;
		@SearchField public final String a2;
		
		public DoubleAttributeEntity(int id, String a1, String a2) {
			this.id = id;
			this.a1 = a1;
			this.a2 = a2;
		}
	}
	
	@Indexable
	public static class DoubleAttributeFiltrableEntity {
		@SearchId public final int id;
		@SearchFilter @SearchField public final String a1;
		@SearchFilter @SearchField public final String a2;
		
		public DoubleAttributeFiltrableEntity(int id, String a1, String a2) {
			this.id = id;
			this.a1 = a1;
			this.a2 = a2;
		}
	}
	
}
