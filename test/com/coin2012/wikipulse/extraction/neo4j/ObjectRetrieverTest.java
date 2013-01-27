package com.coin2012.wikipulse.extraction.neo4j;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

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
		List<WikiEdit> edits = new ArrayList<WikiEdit>();
		edits.add(edit);

		Page page = mock(Page.class);
		when(page.getPageId()).thenReturn("1");
		when(page.getTitle()).thenReturn("Page-A");
		when(page.getCategories()).thenReturn(categories);
		when(page.getEdits()).thenReturn(edits);

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
		id = saver.saveNews(news);
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

}
