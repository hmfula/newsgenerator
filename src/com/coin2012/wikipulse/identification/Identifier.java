package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.models.Title;
import com.google.gson.Gson;

public class Identifier implements Identifiable {
	
	Extractable extractor = new Extractor();

	@Override
	public String getNewsForCategory(String category) {
		
		NewsAlgorithm dummy = new DummyAlgorithm(extractor);
		
		List<Title> edits = dummy.findNews(category);
		Gson gson = new Gson();
		
		
		return gson.toJson(edits);
	}
	
	@Override
	public String getMostReadTitlesForCategory(String category) {
		
		NewsAlgorithm dummy = new DummyAlgorithm(extractor);		
		List<Title> pages = dummy.findMostReadPages(category);
		for (Title title : pages) {
			title.setRelYesterday(dummy.findRelevanceOfPage(title.getTitle()));
		}
		Gson gson = new Gson();		
		return gson.toJson(pages);
	}

}
