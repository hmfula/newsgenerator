package com.coin2012.wikipulse.extraction.statsgrok;

import java.util.List;

import org.restlet.resource.ClientResource;

import com.coin2012.wikipulse.extraction.utils.QueryUtils;
import com.coin2012.wikipulse.models.Page;

public class StatsGrokExtractor {

	public static void updatePagesWithRelevance(List<Page> pages){
		for (Page page : pages) {
			ClientResource resource = StatsGrokQueries.buildQuery(page.getTitle());
			String result = QueryUtils.executeQueryToResource(resource);
			float relevance = StatsGrokResultParser.parseResultToRelevance(result);
			page.setRelYesterday(relevance);
		}
	}
}
