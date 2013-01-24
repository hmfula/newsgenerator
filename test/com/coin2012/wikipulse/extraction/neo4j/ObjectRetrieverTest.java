package com.coin2012.wikipulse.extraction.neo4j;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.coin2012.wikipulse.models.News;

public class ObjectRetrieverTest {
	
	
	@Test
	public void getNews(){
		ObjectRetriever or = new ObjectRetriever();
		List<News> news = or.getNews();
		Assert.assertNull(news);
	}

}
