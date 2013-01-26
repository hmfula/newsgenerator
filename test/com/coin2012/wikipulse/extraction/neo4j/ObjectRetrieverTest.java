package com.coin2012.wikipulse.extraction.neo4j;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;

import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.ShortNews;
import com.coin2012.wikipulse.models.WikiEdit;

public class ObjectRetrieverTest {
	
	private static String id;

	@BeforeClass
	public static void setUp() {
		Category a = new Category();
		a.setTitle("a");
		Category b = new Category();
		b.setTitle("b");
		List<Category> categories = new ArrayList<Category>();
		categories.add(a);
		categories.add(b);

		WikiEdit edit = mock(WikiEdit.class);
		when(edit.getRevid()).thenReturn("999");
		when(edit.getUserId()).thenReturn("user1");
		when(edit.getUser()).thenReturn("USER1");
		WikiEdit edit2 = mock(WikiEdit.class);
		when(edit2.getRevid()).thenReturn("998");
		when(edit2.getUserId()).thenReturn("user2");
		when(edit2.getUser()).thenReturn("USER2");
		List<WikiEdit> edits = new ArrayList<WikiEdit>();
		edits.add(edit);
		edits.add(edit2);
		
		WikiEdit edit3 = mock(WikiEdit.class);
		when(edit3.getRevid()).thenReturn("997");
		when(edit3.getUserId()).thenReturn("user2");
		when(edit3.getUser()).thenReturn("USER2");
		List<WikiEdit> edits2 = new ArrayList<WikiEdit>();
		edits2.add(edit3);
		

		Page page = mock(Page.class);
		when(page.getPageId()).thenReturn("1");
		when(page.getTitle()).thenReturn("Page-A");
		when(page.getCategories()).thenReturn(categories);
		when(page.getEdits()).thenReturn(edits);
		
		Page page2 = mock(Page.class);
		when(page2.getPageId()).thenReturn("2");
		when(page2.getTitle()).thenReturn("Page-B");
		when(page2.getCategories()).thenReturn(categories);
		when(page2.getEdits()).thenReturn(edits2);

		Editor editor = mock(Editor.class);
		when(editor.getUserid()).thenReturn("user1");
		when(editor.getName()).thenReturn("USER1");

		News news = mock(News.class);
		when(news.getPageId()).thenReturn("1");
		when(news.getPagetTitle()).thenReturn("Page-A");
		when(news.getNews()).thenReturn("BigNews");
		when(news.getShortNews()).thenReturn("ShortNews");
		when(news.getTimestamp()).thenReturn("2010");
		when(news.getImageUrlList()).thenReturn(new ArrayList<String>());
		when(news.getEditor()).thenReturn(editor);

		ObjectSaver saver = new ObjectSaver();
		saver.saveOrUpdatePage(page);
		saver.saveOrUpdatePage(page2);
		id = saver.saveNews(news);
	}

	@Test
	public void aTryout(){
		GraphDatabaseService graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		ExecutionEngine engine = new ExecutionEngine(graphDB);
		ExecutionResult result = engine.execute("START nodes = node(*) Return nodes");
		System.out.println("nru ein test");
	}
	@Test
	public void getCategoriesWithHighestNewsCount() {
		ObjectRetriever retriever = new ObjectRetriever();
		List<Category> categories = retriever.getCategoriesWithHighestNewsCount(3);
		Assert.assertTrue(categories.size() == 2);
		for (Category category : categories) {
			Assert.assertTrue(category.getNewsCount() == 1);
		}
	}

	@Test
	public void getSingleNews() {
		ObjectRetriever retriever = new ObjectRetriever();
		News news = retriever.getSingleNews(id);
		Assert.assertTrue(news.getNews().equals("BigNews"));
		Assert.assertTrue(news.getPagetTitle().equals("Page-A"));
		Assert.assertTrue(news.getEditor().getName().equals("USER1"));
	}

	@Test
	public void getNews() {
		ObjectRetriever retriever = new ObjectRetriever();
		List<ShortNews> shortNews = retriever.getNews();
		Assert.assertTrue(shortNews.size() == 1);
		Assert.assertTrue(shortNews.get(0).getPagetTitle().equals("Page-A"));
		Assert.assertTrue(shortNews.get(0).getShortNews().equals("ShortNews"));
		Assert.assertTrue(shortNews.get(0).getEditor().getName().equals("USER1"));

	}

	@Test
	public void getgetLatetestNews() {
		ObjectRetriever retriever = new ObjectRetriever();
		List<ShortNews> shortNews = retriever.getLatetestNews(3);
		Assert.assertTrue(shortNews.size() == 1);
		Assert.assertTrue(shortNews.get(0).getPagetTitle().equals("Page-A"));
		Assert.assertTrue(shortNews.get(0).getShortNews().equals("ShortNews"));
		Assert.assertTrue(shortNews.get(0).getEditor().getName().equals("USER1"));
	}

	@Test
	public void getMostViewedNews() {
		ObjectRetriever retriever = new ObjectRetriever();
		List<ShortNews> shortNews =retriever.getMostViewedNews(3);
		Assert.assertTrue(shortNews.size() == 1);
		Assert.assertTrue(shortNews.get(0).getPagetTitle().equals("Page-A"));
		Assert.assertTrue(shortNews.get(0).getShortNews().equals("ShortNews"));
		Assert.assertTrue(shortNews.get(0).getEditor().getName().equals("USER1"));
	}

	@Test
	public void getNewsByCategory() {
		ObjectRetriever retriever = new ObjectRetriever();
		Category b = new Category();
		b.setTitle("b");
		List<ShortNews> shortNews =retriever.getNewsByCategory(b);
		Assert.assertTrue(shortNews.size() == 1);
		Assert.assertTrue(shortNews.get(0).getPagetTitle().equals("Page-A"));
		Assert.assertTrue(shortNews.get(0).getShortNews().equals("ShortNews"));
		Assert.assertTrue(shortNews.get(0).getEditor().getName().equals("USER1"));
	}

	@Test
	public void getTwoDomainExperts(){
		ObjectRetriever retriever = new ObjectRetriever();
		Category b = new Category();
		b.setTitle("b");
		List<Editor> editors =retriever.getDomainExperts(b, 0);
		Assert.assertTrue(editors.size() == 2);
	}
	
	@Test
	public void getOneDomainExperts(){
		ObjectRetriever retriever = new ObjectRetriever();
		Category b = new Category();
		b.setTitle("b");
		List<Editor> editors =retriever.getDomainExperts(b, 2);
		Assert.assertTrue(editors.size() == 1);
	}
	
	@Test
	public void getTopContributors(){
		ObjectRetriever retriever = new ObjectRetriever();
		List<Editor> editors =retriever.getTopEditors(1);
		Assert.assertTrue(editors.size()==1);
		Assert.assertEquals(editors.get(0).getUserid(), "user2");
	}
	
	@Test
	public void getNewsContributors(){
		ObjectRetriever retriever = new ObjectRetriever();
		List<Editor> editors =retriever.getNewsContributors(1);
		Assert.assertTrue(editors.size()==1);
		Assert.assertEquals(editors.get(0).getUserid(), "user1");
	}
}
