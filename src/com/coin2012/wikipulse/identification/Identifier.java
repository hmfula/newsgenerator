package com.coin2012.wikipulse.identification;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;

public class Identifier implements Identifiable {
	
	Extractable extractor = new Extractor();

	@Override
	public String getNewsForCategory(String category) {
		
		NewsAlgorithm dummy = new DummyAlgorithm(extractor);
		
		dummy.findNews(category);
		// TODO convert List<Newsitem> to JSON/XML/...	
		return null;
	}

}
