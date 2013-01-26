package com.coin2012.wikipulse.extraction.smmry;

import org.restlet.resource.ClientResource;

import com.coin2012.wikipulse.extraction.utils.QueryUtils;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.PageSummary;

public class PageSummarizer {
	
	public static PageSummary summarizeArticle(String url, String length) {
		ClientResource resource = PageSummarizerQueries.buildQueryToSummarizeArticle(url,length);
		String result = QueryUtils.executeQueryToResource(resource);
		PageSummary pageSummary = PageSummarizerResultsParser.parseResultToSummaryPage(result);
		return pageSummary;
	}

}
