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

import com.coin2012.wikipulse.conf.WikipulseConstants;
import com.coin2012.wikipulse.extraction.ExtractorImpl;
import com.coin2012.wikipulse.extraction.neo4j.ObjectRetriever;
import com.coin2012.wikipulse.extraction.neo4j.ObjectSaver;
import com.coin2012.wikipulse.extraction.smmry.PageSummarizer;
import com.coin2012.wikipulse.extraction.smmry.SentenceFinder;
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
		when(editor.getUser()).thenReturn("USER1");

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
		Assert.assertTrue(shortNews.getEditor().getUser().equals("USER1"));
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
		when(wikiEdit.getUserId()).thenReturn("user1");
		when(wikiEdit.getUser()).thenReturn("USER1");
		
		
		
		

		List<Editor> editors = new ArrayList<Editor>();
		Category a = new Category();
		a.setTitle("a");
		Category b = new Category();
		b.setTitle("b");
		List<Category> categories = new ArrayList<Category>();
		categories.add(a);
		categories.add(b);
		
		ExtractorImpl extractor =  new ExtractorImpl();
		ObjectRetriever objectRetriever = mock(ObjectRetriever.class); 
		
		Editor editor1 =  new Editor();
		editor1.setUserid("user1");
		editor1.setUser("USER1");
		
		Editor editor2 =  new Editor();
		editor2.setUserid("user2");
		editor2.setUser("USER2");
		
		Editor editor3 =  new Editor();
		editor3.setUserid("user3");
		editor3.setUser("USER3");
		
		editors.add(editor1);
		editors.add(editor2);
		editors.add(editor3);
		when(objectRetriever.getDomainExperts(a,2)).thenReturn(editors);
		
		
		
		
		Assert.assertTrue("Sorry! The author is unknown by wikipulse",ec.isEditorADomainExpert(extractor, a, wikiEdit, 1));
		
	}
	
	
	@Test
	public void summaryCreatedForEveryPageValidEditOnThePage() {
		Category a = new Category();
		a.setTitle("a");
		Category b = new Category();
		b.setTitle("b");
		List<Category> categories = new ArrayList<Category>();
		categories.add(a);
		categories.add(b);

		
		WikiEdit  wikiEdit1 = new WikiEdit();
		wikiEdit1.setParentid("200");
		wikiEdit1.setRevid("999");
		wikiEdit1.setUserid("user1");
		wikiEdit1.setUser("USER1");
		wikiEdit1.setContent("A journey of a thousand miles always start with one step. Before God all men are created equal.");
		
		WikiEdit  wikiEdit2 = new WikiEdit();
		wikiEdit2.setRevid("1000");
		wikiEdit2.setParentid("200");
		wikiEdit2.setUserid("user2");
		wikiEdit2.setUser("USER2");
		wikiEdit2.setContent("A journey");
		
		WikiEdit  wikiEdit3 = new WikiEdit();
		wikiEdit3.setParentid("200");
		wikiEdit3.setRevid("10001");
		wikiEdit3.setUserid("user3");
		wikiEdit3.setUser("USER3");
		wikiEdit3.setContent("A journey of a thousand miles always start with one step.");
		
		List<WikiEdit> edits = new ArrayList<WikiEdit>();
		edits.add(wikiEdit1);
		edits.add(wikiEdit2);
		edits.add(wikiEdit3);

		Page page = mock(Page.class);
		when(page.getPageId()).thenReturn("1");
		when(page.getTitle()).thenReturn("Page-A");
		when(page.getCategories()).thenReturn(categories);
		when(page.getEdits()).thenReturn(edits);
		String pageContent = "Majority wins. One picture is worthy a thousand words. A journey of a thousand miles always start with one step.  Before God all men are created equal.";
		when(page.getTextContent()).thenReturn(pageContent);

		Editor editor = mock(Editor.class);
		when(editor.getUserid()).thenReturn("user1");
		when(editor.getUser()).thenReturn("USER1");	
		
		System.out.println(page.getEdits().size());
		System.out.println(page.getEdits().get(0).getUser());
		System.out.println(page.getEdits().get(1).getUser());
		System.out.println(page.getEdits().get(2).getUser());
		System.out.println(page.getEdits().get(0).getContent());
		System.out.println(page.getEdits().get(1).getContent());
		System.out.println(page.getEdits().get(2).getContent());
	
		
		WikiEditClassifier ec = new WikiEditClassifier();
		boolean editWorhtiness = ec.isEditNewsWorthy(wikiEdit3);
		System.err.println("news worthy: " + editWorhtiness);
	
		//filter minor edits
		List<WikiEdit>  listOfValuableWikiEdits = ec.filterEditsBasedOnNewsWorthiness(page);
		System.err.println("Number of valid edits: " + listOfValuableWikiEdits.size());
		
		//are edits based on the same page  
		boolean areEditsBasedOnTheSamePage = ec.areEditsBasedOnTheSamePage(wikiEdit1, wikiEdit2 );
		System.err.println("areEditsBasedOnTheSamePage: " + areEditsBasedOnTheSamePage);
		
		//find sentences in a wiki edit
		List<String> sentencesInAWikiEdit = ec.findSentencesInAWikiEdit(wikiEdit1);
		System.err.println("number of sentencesInAWikiEdit (wikiedit1) : " + sentencesInAWikiEdit.size());
		System.err.println("sentencesInAWikiEdit: " + sentencesInAWikiEdit.get(0) +sentencesInAWikiEdit.get(1) );
		
		
		
		//find sentences in a wiki edit3
		sentencesInAWikiEdit = ec.findSentencesInAWikiEdit(wikiEdit3);
		System.err.println("number of sentencesInAWikiEdit (wikiedit3) : " + sentencesInAWikiEdit.size());
		System.err.println("sentencesInAWikiEdit: " + sentencesInAWikiEdit.get(0) );
		
		//find sentences that occur in both edits
		
				List<String> commonSentencesInWikiEdits = ec.findCommonSentencesInWikiEdits(wikiEdit1,wikiEdit3);
				System.err.println("number of commonSentencesInWikiEdits (wikiedit1 and wikiedit3) : " + commonSentencesInWikiEdits.size());
				System.err.println("number of commonSentencesInWikiEdits (wikiedit1 and wikiedit3) : " + commonSentencesInWikiEdits);
				
				//remove a sentences from an edit used to filter edits that appear in that occur in both edits
				System.err.println("removeTextFromAWikiEdit (wikiedit1) Berfore removal : " + wikiEdit1.getContent());
				String contentToRemove = "A journey of a thousand miles always start with one step.";
				WikiEdit removeTextFromAWikiEdit = ec.removeTextFromAWikiEdit(wikiEdit1,contentToRemove);
				
				System.err.println("removeTextFromAWikiEdit (wikiedit1)  After removal ) : " + removeTextFromAWikiEdit.getContent());
				
				
				//summarize a wikiedit wikiedit 2 - not a sentence so no buffering
				System.err.println("summarizedWikiEdit(wikiedit3)  Before summarization : " + wikiEdit2.getContent());
				WikiEdit summarizedWikiEdit = ec.summarizeWikiEditWithBufferSentences(page,wikiEdit2);
				
				
				System.err.println("summarizedWikiEdit (wikiedit3) After summarization : " + summarizedWikiEdit.getContent());
				
				
				//summarize a wikiedit wikiedit 3 - sentence between to sentences from the page  gets buffering before & after wikiedit content
				System.err.println("summarizedWikiEdit(wikiedit3)  Before summarization : " + wikiEdit3.getContent());
				
//				summarizedWikiEdit = ec.summarizeWikiEditWithBufferSentences(page,wikiEdit3);
				
				
				System.err.println("summarizedWikiEdit (wikiedit3) After summarization : " + summarizedWikiEdit.getContent());
				
				//get a combined list of non overlapping sentences from two edits on the same page
				List<String> nonOverllappingWikiEditSentencesFromSamePage = ec.processDuplicateSentencesFromWikiEditsOnTheSamePage(wikiEdit1, wikiEdit3);
				
				
				System.err.println("nonOverllappingWikiEditSentencesFromSamePage (wikiedit1 & wikiedit3) After summarization : " + nonOverllappingWikiEditSentencesFromSamePage);
				
				//Summarize nonOverllappingWikiEditSentencesFromSamePage
				String nonOverlappingWikiEditSummarySentencesForSamePageWikiEdit = ec.getNonOverlappingSummarySentencesForSamePageWikiEdits(page,wikiEdit1, wikiEdit3);
				System.err.println("nonOverlappingWikiEditSummarySentencesForSamePageWikiEdit (wikiedit1 & wikiedit2) Processed sentences  : " + nonOverlappingWikiEditSummarySentencesForSamePageWikiEdit);
				
	
	}
	
	@Test
	public void isEditortADomainExpert() {
		
		
		List<Editor> editors = new ArrayList<Editor>();
		Category a = new Category();
		a.setTitle("a");
		Category b = new Category();
		b.setTitle("b");
		List<Category> categories = new ArrayList<Category>();
		categories.add(a);
		categories.add(b);
		
		ExtractorImpl extractor =  new ExtractorImpl();
		ObjectRetriever objectRetriever = mock(ObjectRetriever.class); 
		
		Editor editor1 =  new Editor();
		editor1.setUserid("user1");
		editor1.setUser("USER1");
		
		Editor editor2 =  new Editor();
		editor2.setUserid("user2");
		editor2.setUser("USER2");
		
		Editor editor3 =  new Editor();
		editor3.setUserid("user3");
		editor3.setUser("USER3");
		
		editors.add(editor1);
		editors.add(editor2);
		editors.add(editor3);
		when(objectRetriever.getDomainExperts(a,2)).thenReturn(editors);
		System.err.println(" Number of Domain experts :  " + extractor.getDomainExperts(a, 2).size());
	
		System.err.println("Domain experts :  " + extractor.getDomainExperts(a, 2).get(1).getUser());
//		WikiEditClassifier ec = new WikiEditClassifier();
		
//		public List<Editor> getOneDomainExperts(Category category, int minEditsInCategory){
	}
//	A journey of a thousand miles always start with one step. Before God all men are created equal.
//	
//	A journey
//	
//	A journey of a thousand miles always start with one step.
//	extractor.summarizeArticle(url, length);
	
//One picture is worthy a thousand words. A journey of a thousand miles always start with one step. Before God all men are created equal.
	//processed edits: A journey , Before God all men are created equal.

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
	
	
	
//    

}
