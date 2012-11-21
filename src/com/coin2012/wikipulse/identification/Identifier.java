package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.SnippetPage;
import com.google.gson.Gson;

public class Identifier implements Identifiable {
	
	Extractable extractor = new Extractor();

	@Override
	public String getNewsForCategory(String category) {
		
		NewsAlgorithm dummy = createDummyAlgorithm();
		
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
	public List<SnippetPage> searchForPagesThatMatch(String searchText) {
		
		return createDummyAlgorithm().searchForPagesThatMatch(searchText);
	}
	
	@Override
	public List<Page> searchForPagesReferencing(String url) {
	
		return createDummyAlgorithm().searchForPagesReferencing(url);
		
	}
	
	private NewsAlgorithm createDummyAlgorithm() {
		NewsAlgorithm dummy = new DummyAlgorithm(extractor);
		return dummy;
	}

}
