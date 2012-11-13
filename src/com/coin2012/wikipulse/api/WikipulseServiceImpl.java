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
}

