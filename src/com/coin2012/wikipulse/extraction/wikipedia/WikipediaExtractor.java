package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.ClientResource;

import com.coin2012.wikipulse.extraction.hsqldb.HsqldbManager;
import com.coin2012.wikipulse.extraction.utils.QueryUtils;
import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;
import com.coin2012.wikipulse.extraction.utils.models.Change;
import com.coin2012.wikipulse.extraction.utils.models.RecentChangesQueryResult;
import com.coin2012.wikipulse.identification.Timespan;
import com.coin2012.wikipulse.models.Editor;
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

	public static void updateEditsWithContent(List<WikiEdit> edits) {
		for (WikiEdit edit : edits) {
			ClientResource resource = WikipediaQueries.buildQueryForRevisionContent(edit.getRevid());
			String result = QueryUtils.executeQueryToResource(resource);
			String content = WikipediaResultParser.parseResultToContent(result, edit.getRevid());
			edit.setContent(content);
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

	public static void enhanceNewsWithImages(List<News> news) {
		for (News newsItem : news) {
			ClientResource resource = WikipediaQueries.buildQueryForImagesFileNames(newsItem.getPagetTitle());
			String result = QueryUtils.executeQueryToResource(resource);
			List<String> imageFileNames = WikipediaResultParser.parseResultToImageFileNames(result);
			if (!imageFileNames.isEmpty()) {
				String imageTitles = imageFileNames.get(0);
				imageFileNames.remove(0);
				if (!imageFileNames.isEmpty()) {
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
		}
		logger.info("Extraction done. Total amount of extracted changes: " + changes.size());
		return changes;
	}

	public static List<Editor> getWikipediaEditors(List<String> editorNamesList) {
		String editorNames = null;
		if (!editorNamesList.isEmpty()) {
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

	public static List<Page> getPagesWithCategoriesForPageIds(List<String> pageids) {
		List<Page> pages = new ArrayList<Page>();
		for (String pageid : pageids) {
			ClientResource resource = WikipediaQueries.buildQueryForPageWithCategoriesByPageId(pageid);
			String result = QueryUtils.executeQueryToResource(resource);
			try{
			Page page = WikipediaResultParser.parseResultToPage(result, pageid);
			pages.add(page);
			}catch(Exception e){
				logger.warning("Pageid '"+pageid+"' could not be created. " + e.getStackTrace());
			}
			
		}
		return pages;
	}

	public static void updatePagesWithEditsInTimespan(List<Page> pages, Timespan timespan) {
		for (Page page : pages) {
			ClientResource resource = WikipediaQueries.buildQueryForRevisionsFromTheLastTwoHours(page.getPageId(), timespan);
			String result = QueryUtils.executeQueryToResource(resource);
			List<WikiEdit> edits = WikipediaResultParser.parseResultToEdits(result, page.getPageId());
			page.setEdits(edits);
		}
	}

}
