package com.coin2012.wikipulse.Identification;

import java.util.LinkedList;
import java.util.List;
import com.coin2012.wikipulse.DomainModel.NewsItem;
import com.coin2012.wikipulse.DomainModel.WikiEdit;
import com.coin2012.wikipulse.Extraction.Extractor;

/**
 *
 * Dummy Algorithm that just converts WikiEdits to NewsItems.
 * 
 * @author Stefan
 *
 */
public class DummyAlgorithm implements NewsAlgorithm {
	
	private Extractor extractor;

	public DummyAlgorithm(Extractor extractor) {
		this.extractor = extractor;
	}
	
	@Override
	public List<NewsItem> findNews(String category) {
		
		LinkedList<NewsItem> news = new LinkedList<NewsItem>();
		List<WikiEdit> edits = extractor.getLatestEdits(category, 10);
		
		for (WikiEdit edit : edits) {
			NewsItem tmp = new NewsItem();
			tmp.setCategory(category);
			tmp.setContent(edit.getContent());
			tmp.setTitle(edit.getRevid());
			news.add(tmp);
		}
		
		return news;
	}
}
