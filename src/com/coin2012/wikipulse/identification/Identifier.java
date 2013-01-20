package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.extraction.neo4j.AuthorgraphDatabase;
import com.coin2012.wikipulse.extraction.smmry.PageSummarizer;
import com.coin2012.wikipulse.identification.newsselection.Authorgraph;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;

public class Identifier implements Identifiable {
	
	Extractable extractor = new Extractor();

	public Identifier() {
		// init Authorgraph thread if not running (TODO: probably better done in main init method from application)
		Authorgraph.initAuthorgraphDatabaseThread(extractor);
	}
	
	@Override
	public List<News> getNews() {
		
		return AuthorgraphDatabase.getLatestNews();
	}
	
	
	public Page summarizeArticle(String url, String length) {
		Page page = PageSummarizer.summarizeArticle(url, length);
		return page;
	}
}
