package com.coin2012.wikipulse.extraction.neo4j;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class ObjectRetrieverTest {
	
	@BeforeClass
	public static void setUp(){
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
		saver.saveNews(news);
	
	}
	
	
	@Test
	public void getCategoriesWithHighestNewsCount(){
		ObjectRetriever retriever = new ObjectRetriever();
		retriever.getCategoriesWithHighestNewsCount(3);
	}
	@Test
	public void getSingleNews(){
		ObjectRetriever retriever = new ObjectRetriever();
		retriever.getSingleNews("1");
	}
	
	@Test
	public void getNews(){
		ObjectRetriever retriever = new ObjectRetriever();
		retriever.getNews();
	}
	
	@Test
	public void getgetLatetestNews(){
		ObjectRetriever retriever = new ObjectRetriever();
		retriever.getLatetestNews(3);
	}
	
	@Test
	public void getMostViewedNews(){
		ObjectRetriever retriever = new ObjectRetriever();
		retriever.getMostViewedNews(3);
	}
	@Test
	public void getNewsByCategory(){
		ObjectRetriever retriever = new ObjectRetriever();
		Category b = new Category();
		b.setTitle("b");
		retriever.getNewsByCategory(b);
	}


}
