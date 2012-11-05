package com.coin2012.wikipulse.Identification;

import com.coin2012.wikipulse.Extraction.Extractor;

public class IdentifierImpl implements Identifier {
	
	Extractor extractor;

	public IdentifierImpl(Extractor extractor) {
		this.extractor = extractor;
	}
	
	@Override
	public String getNews(String category) {
		
		NewsAlgorithm dummy = new DummyAlgorithm(extractor);
		
		dummy.findNews(category);
		// TODO convert List<Newsitem> to JSON/XML/...	
		return null;
	}

}
