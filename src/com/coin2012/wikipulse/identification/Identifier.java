package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.models.Page;
import com.google.gson.Gson;

public class Identifier implements Identifiable {
	
	Extractable extractor = new Extractor();

	@Override
	public String getNewsForCategory(String category) {
		
		NewsAlgorithm dummy = new DummyAlgorithm(extractor);
		
		List<Page> edits = dummy.findNews(category);
		Gson gson = new Gson();
		
		
		return gson.toJson(edits);
	}
	
	@Override
	public String getMostReadTitlesForCategory(String category) {
		
		NewsAlgorithm dummy = new DummyAlgorithm(extractor);		
		List<Page> pages = dummy.findMostReadPages(category);
		Gson gson = new Gson();		
		return gson.toJson(pages);
	}

}
