package com.coin2012.wikipulse.extraction;

import java.util.List;

import com.coin2012.wikipulse.extraction.statsgrok.StatsGrokExtractor;
import com.coin2012.wikipulse.extraction.wikipedia.WikipediaExtractor;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.SnippetPage;

/**
 * bla test commit
 * @author Karsten
 *
 */
public class Extractor implements Extractable {

	@Override
	public List<Page> getTitlesForCategory(String category) {
		List<Page> pages = WikipediaExtractor.getPagesForCategory(category);
		WikipediaExtractor.updatePagesWithEdits(pages);
		StatsGrokExtractor.updatePagesWithRelevance(pages);
		return pages;
	}
	
	
	@Override
	
	public List<SnippetPage> searchForPagesThatMatch(String searchText) {
		List<SnippetPage> pages = WikipediaExtractor.searchForPagesThatMatch(searchText);
		return pages;
		
	}
	
	
	@Override
	public List<Page> searchForPagesReferencing(String url){
		List<Page> pages = WikipediaExtractor.searchForPagesReferencing(url);
		return pages;
		
	}
	
}
