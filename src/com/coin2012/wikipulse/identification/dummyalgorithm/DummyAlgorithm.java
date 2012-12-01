package com.coin2012.wikipulse.identification.dummyalgorithm;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.identification.NewsAlgorithm;
import com.coin2012.wikipulse.identification.PageImageDetails;
//import com.coin2012.wikipulse.models.NewsItem;
import com.coin2012.wikipulse.models.Page;
//import com.coin2012.wikipulse.models.WikiEdit;
import com.coin2012.wikipulse.models.SnippetPage;

/**
 *
 * Dummy Algorithm that just converts WikiEdits to NewsItems.
 * 
 * @author Stefan
 *
 */
public class DummyAlgorithm implements NewsAlgorithm {
	
	private Extractable extractor;

	public DummyAlgorithm(Extractable extractor) {
		this.extractor = extractor;
	}
	
	@Override
	public List<Page> findNews(String category) {
		
		List<Page> edits = extractor.getTitlesForCategory(category);
		
//		for (WikiEdit edit : edits) {
//			NewsItem tmp = new NewsItem();
//			tmp.setCategory(category);
//			tmp.setContent(edit.getContent());
//			tmp.setTitle(edit.getRevid());
//			news.add(tmp);
//		}
		
		return edits;
	}
	
	@Override
	public List<Page> findMostReadPages(String category) {
		List<Page> pages = extractor.getTitlesForCategory(category);	
		return pages;
	}

	
	@Override
	public List<SnippetPage> searchForPagesThatMatch(String searchText) {
		return extractor.searchForPagesThatMatch(searchText);
	}
	
	@Override
	public List<Page> searchForPagesReferencing(String url) {
		return 	extractor.searchForPagesReferencing(url);
	}

	@Override
	public List<PageImageDetails> getImageDetailsRelatedTo(String subjectTitle) {
		return extractor.getImageDetailsRelatedTo(subjectTitle);
	}
}
