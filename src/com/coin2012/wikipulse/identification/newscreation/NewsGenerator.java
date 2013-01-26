package com.coin2012.wikipulse.identification.newscreation;

import java.util.ArrayList;
import java.util.List;

import com.coin2012.wikipulse.extraction.smmry.PageSummarizer;
import com.coin2012.wikipulse.extraction.smmry.SentenceFinder;
import com.coin2012.wikipulse.extraction.utils.WikipulseConstants;
import com.coin2012.wikipulse.identification.newscreation.classification.WikiEditClassifier;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.PageSummary;
import com.coin2012.wikipulse.models.WikiEdit;

public class NewsGenerator {


	public static List<News> createNewsFromPages(List<Page> pages) {
		List<News> newsList = new ArrayList<News>();
		WikiEditClassifier wikiEditClassifier = new WikiEditClassifier();
		
		for (Page page: pages) {
			
			wikiEditClassifier.classify(page);//classify the pages based on edits
		
		
		WikiEdit wikiEdit = new WikiEdit();//Dummy
		String bufferedWikiEdit = SentenceFinder.summarize(page.getTextContent(), wikiEdit.getContent());//TODO create buffered summaries - use own impl or smmry//REMEMBER get to remove get title
		News item = new News();	//create and return news 
		item.setPageId(page.getPageId());
		item.setPagetTitle(page.getTitle());
		item.setRelYesterday(page.getRelYesterday());
		//if there are more that 30 sentences in the buffered WikiEdit,  we use the smmry api to summarize to 20 sentences
		if(SentenceFinder.getNumberOfSentencesInBufferedWikiEdit() > WikipulseConstants.MAXIMUM_ALLOWED_NUMBER_OF_SENTENCES){
			PageSummary summarizedEdit =	PageSummarizer.summarizeArticle(page.getPageUrl(), Integer.toString(WikipulseConstants.MAXIMUM_ALLOWED_NUMBER_OF_SENTENCES));
			item.setNews(summarizedEdit.getSm_api_content());
		}
		else{
			
			item.setNews(bufferedWikiEdit);
		}
		
			Editor editor = new Editor();
			editor.setUserid(wikiEdit.getUserId());
			editor.setName(wikiEdit.getUser());
			item.setEditor(editor);
		
			newsList.add(item);
		}
		return newsList;

	}
}
