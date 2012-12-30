package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.ClientResource;

import com.coin2012.wikipulse.extraction.Editor;
import com.coin2012.wikipulse.extraction.hsqldb.HsqldbManager;
import com.coin2012.wikipulse.extraction.utils.QueryUtils;
import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;
import com.coin2012.wikipulse.extraction.utils.models.Change;
import com.coin2012.wikipulse.extraction.utils.models.RecentChangesQueryResult;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class WikipediaExtractor {

	static Logger logger = Logger.getLogger("WikipediaExtractor.class");

	public static List<Page> getPagesForCategory(String category) {
		ClientResource resource = WikipediaQueries.buildQueryForPagesInCategory(category);
		String result = QueryUtils.executeQueryToResource(resource);
		List<Page> titles = WikipediaResultParser.parseResultToPages(result);

		return titles;
	}

	public static void updatePagesWithEdits(List<Page> pages) {
		for (Page page : pages) {
			ClientResource resource = WikipediaQueries.buildQueryForRevisions(page.getPageId());
			String result = QueryUtils.executeQueryToResource(resource);
			List<WikiEdit> edits = WikipediaResultParser.parseResultToEdits(result, page.getPageId());
			page.setEdits(edits);
		}
	}

	public static List<Change> getRecentChangesWithinTwoHours() {
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
	
	public static void enhanceNewsWithImages(List<News> news){
		for (News newsItem : news) {
			ClientResource resource = WikipediaQueries.buildQueryForImagesFileNames(newsItem.getPagetTitle());
			String result = QueryUtils.executeQueryToResource(resource);
			List<String> imageFileNames = WikipediaResultParser.parseResultToImageFileNames(result);
			if(!imageFileNames.isEmpty()){
				String imageTitles = imageFileNames.get(0);
				imageFileNames.remove(0);
				for (String string : imageFileNames) {
					imageTitles = imageTitles + "|" + string;
				}
				ClientResource imageUrlResource = WikipediaQueries.buildQueryForImagesUrlsOfImageFileNames(imageTitles);
				String imgUrlQueryResult = QueryUtils.executeQueryToResource(imageUrlResource);
				List<String> imageUrls = WikipediaResultParser.parseResultToImageURLs(imgUrlQueryResult);
				newsItem.getImageUrlList().addAll(imageUrls);
			}
		}
	}
	
	/**
	 * Returns a list of changes within a given time frame hours
	 * 
	 * @return
	 */
	private static List<Change> getRecentChanges(String now, String timestamp) {

		ClientResource resource = WikipediaQueries.buildQueryForRecentChanges(now, timestamp);
		logger.info("Starting extracting recent changes from " + now + " till " + timestamp);
		String result = QueryUtils.executeQueryToResource(resource);
		RecentChangesQueryResult parsedResult = WikipediaResultParser.parseResultToRecentChangesQueryResult(result);
		List<Change> changes = parsedResult.getChanges();
		String rcstart = parsedResult.getRcstart();
		while (rcstart != null) {
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

	public static List<Editor> getWikipediaEditors(List <String> editorNamesList) {
		String editorNames = null;
		if(!editorNamesList.isEmpty()){
			editorNames = editorNamesList.remove(0);
			
			for (String currentEditorName : editorNamesList) {
				editorNames = editorNames + "|" + currentEditorName;
			}
		}
		ClientResource resource = WikipediaQueries.buildQueryToFetchWikipediaEditors(editorNames);
		String result = QueryUtils.executeQueryToResource(resource);
		List<Editor> editors = WikipediaResultParser.parseResultToMatchingEditors(result);
		return editors;
	}

//	public static List<SnippetPage> searchForPagesThatMatch(String searchText) {
//		ClientResource resource = WikipediaQueries.buildQuerySearchForPagesThatMatch(searchText);
//		String result = QueryUtils.executeQueryToResource(resource);
//		List<SnippetPage> pages = WikipediaResultParser.parseResultToPageSnippets(result);
//		return pages;
//	}

//	public static List<Page> searchForPagesReferencing(String url) {
//		ClientResource resource = WikipediaQueries.buildQueryToSearchForPagesReferencing(url);
//		String result = QueryUtils.executeQueryToResource(resource);
//		List<Page> pages = WikipediaResultParser.parseResultToMatchingPages(result);
//		return pages;
//	}
}
