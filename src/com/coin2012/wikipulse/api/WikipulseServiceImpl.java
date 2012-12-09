package com.coin2012.wikipulse.api;

import java.util.List;

import com.coin2012.wikipulse.extraction.AggregatedChanges;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.identification.Identifiable;
import com.coin2012.wikipulse.identification.Identifier;
import com.google.gson.Gson;

/**
 * WikiPulseService. Implementation based on the facade design pattern.
 * 
 * @author harrison mfula
 * @author karsten packmohr
 * @since  10-11-2012
 */
public class WikipulseServiceImpl implements WikipulseService {

	@Override
	public String getNewsForCategory(String category) {
		Identifiable identifier = new Identifier();
		String news = identifier.getNewsForCategory(category);
		return news;
	}
	
	@Override
	public String getMostReadTitlesForCategory(String category){
		Identifiable identifier = new Identifier();
		String news = identifier.getMostReadTitlesForCategory(category);
		return news;		
	}
	
	@Override
	public String searchForPagesThatMatch(String searchText){
		Identifiable identifier = new Identifier();
		String snippetPages = identifier.searchForPagesThatMatch(searchText);
		return snippetPages;	
	}
	
	@Override
	public String searchForPagesReferencing(String url){
		Identifiable identifier = new Identifier();
		String pages = identifier.searchForPagesReferencing(url);
		return pages;	
	}

	@Override
	public String getPageWithImages(String pageTitle) {
		return new Identifier().getPageWithImages(pageTitle);
	}

	@Override
	public String getRecentChanges() {
		Gson gson = new Gson();
		Extractor ex = new Extractor();
		List<AggregatedChanges> list = ex.getRecentChanges();
		String result = gson.toJson(list);
		
		return result;
	}
}

