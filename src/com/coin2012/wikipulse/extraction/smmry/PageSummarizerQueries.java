package com.coin2012.wikipulse.extraction.smmry;

import org.restlet.resource.ClientResource;

public class PageSummarizerQueries {
	private static final String SMMRY_URL = "http://api.smmry.com/";
	private static final String SM_API_KEY = "792375072138384";
	
	/**
	 * Example http://api.smmry.com/&SM_API_KEY=792375072138384&SM_LENGTH=5&SM_URL=http://en.wikipedia.org/wiki/The_A-Team
	 * 
	 *Possible parameters placed in the request URL are:
	 * 
	 * &SM_API_KEY=N // Mandatory, N represents your registered API key.
	 * &SM_URL=X // Optional, X represents the web page to summarize.
	 * &SM_LENGTH=N // Optional, N represents the number of sentences returned, default is 6
	 * &SM_KEYWORD_COUNT=N // Optional, N represents how many of the top keywords to return
	 * &SM_QUOTE_AVOID // Optional, summary will not include quotations
	 * &SM_WITH_BREAK // Optional, summary will contain string [BREAK] between each sentence
	 */
	public static ClientResource buildQueryToSummarizeArticle(String url,
			String length) {

		ClientResource resource = new ClientResource(SMMRY_URL);
		resource.getReference().addQueryParameter("SM_API_KEY", SM_API_KEY);
		resource.getReference().addQueryParameter("SM_KEYWORD_COUNT", "6");//TODO fix hard code
		resource.getReference().addQueryParameter("SM_LENGTH", length);
		resource.getReference().addQueryParameter("SM_URL", url );
		resource.getReference().addSegment("&amp;SM_QUOTE_AVOID");//TODO fix hard code
		resource.getReference().addSegment("&amp;SM_WITH_BREAK");//TODO fix hard code
		return resource;
	}

}
