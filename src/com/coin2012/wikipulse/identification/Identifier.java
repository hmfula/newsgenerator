package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.identification.dummyalgorithm.DummyAlgorithm;
import com.coin2012.wikipulse.identification.significancealgorithm.SignificanceAlgorithm;
import com.coin2012.wikipulse.models.Page;
import com.google.gson.Gson;

public class Identifier implements Identifiable {
	
	Extractable extractor = new Extractor();

	@Override
	public String getNewsForCategory(String category) {
		
		NewsAlgorithm dummy = createSignificanceAlgorithm();
		
		List<Page> edits = dummy.findNews(category);
		Gson gson = new Gson();
		
		
		return gson.toJson(edits);
	}

	
	
	@Override
	public String getMostReadTitlesForCategory(String category) {
		
		NewsAlgorithm dummy = createDummyAlgorithm();		
		List<Page> pages = dummy.findMostReadPages(category);
		Gson gson = new Gson();		
		return gson.toJson(pages);
	}
	
	
	@Override
	public String searchForPagesThatMatch(String searchText) {
		
		Gson gson = new Gson();		
		return gson.toJson(createDummyAlgorithm().searchForPagesThatMatch(searchText));
	}
	
	@Override
	public String  searchForPagesReferencing(String url) {
	
		Gson gson = new Gson();		
		return gson.toJson(createDummyAlgorithm().searchForPagesReferencing(url));
		
	}
	
	private NewsAlgorithm createDummyAlgorithm() {
		NewsAlgorithm dummy = new DummyAlgorithm(extractor);
		return dummy;
	}
	
	private NewsAlgorithm createSignificanceAlgorithm() {
		NewsAlgorithm alg = new SignificanceAlgorithm(extractor);
		return alg;
	}

}
