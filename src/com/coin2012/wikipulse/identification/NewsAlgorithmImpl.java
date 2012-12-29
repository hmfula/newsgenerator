package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.identification.newscreation.Dummy;
import com.coin2012.wikipulse.identification.newsselection.Authorgraph;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;

public class NewsAlgorithmImpl implements NewsAlgorithm {

	private Extractable extractor;
	
	public NewsAlgorithmImpl(Extractable extractor) {
		this.extractor = extractor;
	}

	@Override
	public List<News> findNews(String category) {
		// get pages from extraction
		List<Page> pageList = extractor.getPagesForCategory(category);
		
		// select pages that are news-worthy
		List<Page> resultSet1 = Authorgraph.findInterestingPages(pageList);
		
		
		// intersect result sets
		
		
		// extract information from pages and generate news
		List <News> newsResults = Dummy.createNewsFromPages(resultSet1); // Dummy for now
		
		return newsResults;
	}

}
