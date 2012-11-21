package com.coin2012.wikipulse.api;

import java.util.ArrayList;
import java.util.List;

import com.coin2012.wikipulse.identification.Identifiable;
import com.coin2012.wikipulse.identification.Identifier;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.SnippetPage;

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
	public List<SnippetPage> searchForPagesThatMatch(String searchText){
		 List<SnippetPage> snippetPages = new ArrayList<SnippetPage>();
		Identifiable identifier = new Identifier();
		snippetPages = identifier.searchForPagesThatMatch(searchText);
		return snippetPages;	
	}
	
	@Override
	public List<Page> searchForPagesReferencing(String url){
		 List<Page> pages = new ArrayList<Page>();
		Identifiable identifier = new Identifier();
		pages = identifier.searchForPagesReferencing(url);
		return pages;	
	}
}

