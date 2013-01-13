package com.coin2012.wikipulse.identification;

import java.util.LinkedList;
import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.extraction.smmry.PageSummarizer;
import com.coin2012.wikipulse.identification.newscreation.Dummy;
import com.coin2012.wikipulse.identification.newsselection.Authorgraph;
import com.coin2012.wikipulse.identification.newsselection.CommonWorkingSetAuthors;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;

public class Identifier implements Identifiable {
	
	Extractable extractor = new Extractor();

	public Identifier() {
		// init Authorgraph thread if not running (TODO: probably better done in main init method from application)
		Authorgraph.initAuthorgraphDatabaseThread(extractor);
	}
	
	@Override
	public List<News> getNewsForCategory(String category) {
		
		// get working set from extraction
		List<Page> pageList = extractor.getPagesForCategory(category);
		extractor.enhancePagesWithRelevance(pageList);
		extractor.enhancePagesWithEdits(pageList);
		
		// select pages that are news-worthy
		Authorgraph.rankPages(pageList);
		CommonWorkingSetAuthors.rankPages(pageList, extractor);
		
		
		// create and sort result set
		List<Page> resultSet = new LinkedList<Page>();
		
		for (Page p: pageList) {
			int i;
			
			for (i = 0; i < resultSet.size(); i++) {
				if ((p.getAuthorgraphRank()+p.getCommonWorkingSetAuthorsRank()) > (resultSet.get(i).getAuthorgraphRank()+resultSet.get(i).getCommonWorkingSetAuthorsRank())) {
					resultSet.add(i, p);
					break;
				}
			}
			
			if (i == resultSet.size()) {
				resultSet.add(p);
			}
		}
		
		// extract information from pages and generate news
		List <News> newsResults = Dummy.createNewsFromPages(resultSet); // dummy for now
		
		return newsResults;
	}
	
	
	public Page summarizeArticle(String url, String length) {
		Page page = PageSummarizer.summarizeArticle(url, length);
		return page;
	}
}
