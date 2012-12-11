package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.ClientResource;

import com.coin2012.wikipulse.extraction.hsqldb.HsqldbManager;
import com.coin2012.wikipulse.extraction.utils.QueryUtils;
import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.SnippetPage;
import com.coin2012.wikipulse.models.WikiEdit;

public class WikipediaExtractor {

	static Logger logger = Logger.getLogger("WikipediaExtractor.class");
	
	public static List<Page> getPagesForCategory(String category) {
		ClientResource resource = WikipediaQueries
				.buildQueryForPagesInCategory(category);
		String result = QueryUtils.executeQueryToResource(resource);
		List<Page> titles = WikipediaResultParser.parseResultToTitles(result);

		return titles;
	}

	public static void updatePagesWithEdits(List<Page> pages) {
		for (Page page : pages) {
			ClientResource resource = WikipediaQueries
					.buildQueryForRevisions(page.getPageId());
			String result = QueryUtils.executeQueryToResource(resource);
			List<WikiEdit> edits = WikipediaResultParser.parseResultToEdits(
					result, page.getPageId());
			page.setEdits(edits);
		}

	}
	
	public static List<Change> getRecentChangesWithinTwoHours(){
		String currentTimestamp = TimestampGenerator.generateTimestamp();
		String timestamp = HsqldbManager.getTimestampForLastSavedChange();
		String queryTimestamp = "";
		if (timestamp != null) {
			Date now = TimestampGenerator.generateDateForTimestamp(currentTimestamp);
			Date lastTimestampDate = TimestampGenerator.generateDateForTimestamp(timestamp);
			long diffInHours = (lastTimestampDate.getTime() - now.getTime()) / (1000 * 60 * 60);
			if (diffInHours < 2) {
				queryTimestamp = timestamp;
			} else {
				queryTimestamp = TimestampGenerator.generateTimestampFromTwoHoursAgo();
			}
		} else {
			queryTimestamp = TimestampGenerator.generateTimestampFromTwoHoursAgo();
		}
		List<Change> changes = WikipediaExtractor.getRecentChanges(currentTimestamp, queryTimestamp);
		return changes;
	}

	/**
	 * Returns a list Pages changes within a given time frame hours
	 * 
	 * @return
	 */
	public static List<Change> getRecentChanges(String now, String timestamp) {

		ClientResource resource = WikipediaQueries.buildQueryForRecentChanges(now, timestamp);
		logger.info("Starting extracting recent changes from " + now + " till " + timestamp);
		String result = QueryUtils.executeQueryToResource(resource);
		RecentChangesQueryResult parsedResult = WikipediaResultParser.parseResultToRecentChangesQueryResult(result);
		List<Change> changes = parsedResult.getChanges();
		String rcstart = parsedResult.getRcstart();
		while(rcstart != null){
			resource = WikipediaQueries.buildQueryForRecentChanges(rcstart, timestamp);
			result = QueryUtils.executeQueryToResource(resource);
			parsedResult = WikipediaResultParser.parseResultToRecentChangesQueryResult(result);
			rcstart = parsedResult.getRcstart();
			changes.addAll(parsedResult.getChanges());
			logger.info("Currently queried amount of changes: " + changes.size());
		}
		logger.info("Extraction done. Total amount of extracted changes: " + changes.size());
		return changes;
	}

	public static List<SnippetPage> searchForPagesThatMatch(String searchText) {
		ClientResource resource = WikipediaQueries
				.buildQuerySearchForPagesThatMatch(searchText);
		String result = QueryUtils.executeQueryToResource(resource);
		List<SnippetPage> pages = WikipediaResultParser
				.parseResultToPageSnippets(result);
		return pages;
	}

	public static List<Page> searchForPagesReferencing(String url) {
		ClientResource resource = WikipediaQueries
				.buildQueryToSearchForPagesReferencing(url);
		String result = QueryUtils.executeQueryToResource(resource);
		List<Page> pages = WikipediaResultParser
				.parseResultToMatchingPages(result);
		return pages;
	}

	public static List<Page> getPageWithImages(String pageTitle) {
		ClientResource resource = WikipediaQueries
				.buildQueryToSearchForImagesAbout(pageTitle);
		String result = QueryUtils.executeQueryToResource(resource);
		List<Page> pages = WikipediaResultParser.parseResultPages(result);
		return pages;
	}

}
