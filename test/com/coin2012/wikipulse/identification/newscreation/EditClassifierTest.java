package com.coin2012.wikipulse.identification.newscreation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.extraction.neo4j.ObjectRetriever;
import com.coin2012.wikipulse.extraction.neo4j.ObjectSaver;
import com.coin2012.wikipulse.extraction.smmry.PageSummarizer;
import com.coin2012.wikipulse.extraction.smmry.SentenceFinder;
import com.coin2012.wikipulse.extraction.utils.WikipulseConstants;
import com.coin2012.wikipulse.identification.newscreation.classification.WikiEditClassifier;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.PageSummary;
import com.coin2012.wikipulse.models.ShortNews;
import com.coin2012.wikipulse.models.WikiEdit;

public class EditClassifierTest {
	 News news;
	private WikiEdit wikiEdit;
	@Before
	public void setUp() {
		Category a = new Category();
		a.setTitle("a");
		Category b = new Category();
		b.setTitle("b");
		List<Category> categories = new ArrayList<Category>();
		categories.add(a);
		categories.add(b);

		wikiEdit = mock(WikiEdit.class);
		when(wikiEdit.getRevid()).thenReturn("999");
		when(wikiEdit.getUserId()).thenReturn("user1");
		when(wikiEdit.getUser()).thenReturn("USER1");
		when(wikiEdit.getContent()).thenReturn("A journey of a thousand miles always start with one step.");
		List<WikiEdit> edits = new ArrayList<WikiEdit>();
		edits.add(wikiEdit);

		Page page = mock(Page.class);
		when(page.getPageId()).thenReturn("1");
		when(page.getTitle()).thenReturn("Page-A");
		when(page.getCategories()).thenReturn(categories);
		when(page.getEdits()).thenReturn(edits);

		Editor editor = mock(Editor.class);
		when(editor.getUserid()).thenReturn("user1");
		when(editor.getName()).thenReturn("USER1");

		news = mock(News.class);
		when(news.getPageId()).thenReturn("1");
		when(news.getPagetTitle()).thenReturn("Page-A");
		when(news.getNews()).thenReturn("BigNews");
		when(news.getShortNews()).thenReturn("ShortNews");
		when(news.getTimestamp()).thenReturn("2010");
		when(news.getImageUrlList()).thenReturn(new ArrayList<String>());
		when(news.getEditor()).thenReturn(editor);
		when(news.getPageLink()).thenReturn("testUrl");

		ObjectSaver saver = mock(ObjectSaver.class);
		
		saver.saveOrUpdatePage(page);
		saver.saveNews(news);
		saver.updateViewCount("1");

	}
	
	@Test
	public void classifyPageEdit() {
		List<ShortNews> shortNewsList  = new ArrayList<ShortNews>();
		shortNewsList.add(news);
		ObjectRetriever retriever = mock(ObjectRetriever.class);
		when(retriever.getNews()).thenReturn(shortNewsList);
		Assert.assertTrue(shortNewsList.size() == 1);
		ShortNews shortNews = shortNewsList.get(0);
		Assert.assertTrue(shortNews.getPagetTitle().equals("Page-A"));
		Assert.assertTrue(shortNews.getShortNews().equals("ShortNews"));
		Assert.assertTrue(shortNews.getEditor().getName().equals("USER1"));
		Assert.assertTrue(shortNews.getPageLink().equals("testUrl"));
		
		WikiEditClassifier ec = new WikiEditClassifier();
		boolean editWorhtiness = ec.isEditNewsWorthy(wikiEdit);
		System.err.println(editWorhtiness);
		Assert.assertTrue("Typo fixes are not regarded as news valid edits",editWorhtiness);
		

	}
	
	@Test
	public void isEditorANewsCreator() {
		WikiEditClassifier ec = new WikiEditClassifier();
		
		wikiEdit = mock(WikiEdit.class);
		when(wikiEdit.getRevid()).thenReturn("1");
		when(wikiEdit.getUserId()).thenReturn("Koavf");
		when(wikiEdit.getUser()).thenReturn("Koavf");
		
		Assert.assertTrue("Sorry! The author is unknown by wikipulse",ec.isEditorANewsCreator(wikiEdit));
		
	}
	
	
//	extractor.summarizeArticle(url, length);
	


//	public static List<News> createNewsFromPages(List<Page> pages) {
//		List<News> resultSet = new LinkedList<News>();
//		List<News> newsList = new ArrayList<News>();
//		WikiEditClassifier wikiEditClassifier = new WikiEditClassifier();
//		
//		for (Page page: pages) {
//			
//			wikiEditClassifier.classify(page);//classify the pages based on edits
//		
//		
//		WikiEdit wikiEdit = new WikiEdit();//Dummy
//		String bufferedWikiEdit = SentenceFinder.summarize(page.getTextContent(), wikiEdit.getContent());//TODO create buffered summaries - use own impl or smmry//REMEMBER get to remove get title
//		News item = new News();	//create and return news 
//		item.setPageId(page.getPageId());
//		item.setPagetTitle(page.getTitle());
//		item.setRelYesterday(page.getRelYesterday());
//		//if there are more that 30 sentences in the buffered WikiEdit,  we use the smmry api to summarize to 20 sentences
//		if(SentenceFinder.getNumberOfSentencesInBufferedWikiEdit() > WikipulseConstants.MAXIMUM_ALLOWED_NUMBER_OF_SENTENCES){
//			String pageUrl = WikipulseConstants.WIKIPEDIA_API_BASE_URL+"/"+ page.getTitle();
//			PageSummary summarizedEdit =	PageSummarizer.summarizeArticle(pageUrl, Integer.toString(WikipulseConstants.MAXIMUM_ALLOWED_NUMBER_OF_SENTENCES));
//			item.setNews(summarizedEdit.getSm_api_content());
//		}
//		else{
//			
//			item.setNews(bufferedWikiEdit);
//		}
//		
//			Editor editor = new Editor();
//			editor.setUserid(wikiEdit.getUserId());
//			editor.setName(wikiEdit.getUser());
//			item.setEditor(editor);
//		
//			newsList.add(item);
//		}
//		return newsList;
}
