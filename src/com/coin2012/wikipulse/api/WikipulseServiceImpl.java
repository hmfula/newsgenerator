package com.coin2012.wikipulse.api;

import com.coin2012.wikipulse.identification.Identifiable;
import com.coin2012.wikipulse.identification.Identifier;

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
	public String getImageDetailsRelatedTo(String imageTitle) {
		return new Identifier().getImageDetailsRelatedTo(imageTitle);
	}
}

