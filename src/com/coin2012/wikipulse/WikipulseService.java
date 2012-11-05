package com.coin2012.wikipulse;

import com.coin2012.wikipulse.Extraction.Extractor;
import com.coin2012.wikipulse.Identification.Identifier;
import com.coin2012.wikipulse.Identification.IdentifierImpl;

public class WikipulseService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Extractor ex = new ExtractorImpl();
		Identifier id = new IdentifierImpl(ex);
		
		System.out.println(id.getNews("United_States_presidential_election,_2012"));

	}

}
