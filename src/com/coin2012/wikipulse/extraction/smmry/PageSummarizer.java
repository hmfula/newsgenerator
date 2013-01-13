package com.coin2012.wikipulse.extraction.smmry;

import org.restlet.resource.ClientResource;

import com.coin2012.wikipulse.extraction.utils.QueryUtils;
import com.coin2012.wikipulse.models.Page;

public class PageSummarizer {
	
	public static Page summarizeArticle(String url, String length) {
		ClientResource resource = PageSummarizerQueries.buildQueryToSummarizeArticle(url,length);
		String result = QueryUtils.executeQueryToResource(resource);
		Page page = PageSummarizerResultsParser.parseResultToSummaryPage(result);
		return page;
	}

}
