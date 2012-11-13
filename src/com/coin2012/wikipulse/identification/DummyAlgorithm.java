package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
//import com.coin2012.wikipulse.models.NewsItem;
import com.coin2012.wikipulse.models.Page;
//import com.coin2012.wikipulse.models.WikiEdit;

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
}
