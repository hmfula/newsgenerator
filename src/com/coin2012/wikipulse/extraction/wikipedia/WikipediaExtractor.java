package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.List;

import org.restlet.resource.ClientResource;

import com.coin2012.wikipulse.extraction.utils.QueryUtils;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.SnippetPage;
import com.coin2012.wikipulse.models.WikiEdit;

public class WikipediaExtractor {
	
	public static List<Page> getPagesForCategory(String category) {
		ClientResource resource = WikipediaQueries.buildQueryForPagesInCategory(category);
		String result = QueryUtils.executeQueryToResource(resource);
		List<Page> titles = WikipediaResultParser.parseResultToTitles(result);

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
	
	public static List<SnippetPage> searchForPagesThatMatch(String searchText) {
		ClientResource resource = WikipediaQueries.buildQuerySearchForPagesThatMatch(searchText);
		String result = QueryUtils.executeQueryToResource(resource);
		List<SnippetPage> pages = WikipediaResultParser.parseResultToPageSnippets(result);
		return pages;
	}
	
	public static List<Page> searchForPagesReferencing(String url) {
		ClientResource resource = WikipediaQueries.buildQueryToSearchForPagesReferencing(url);
		String result = QueryUtils.executeQueryToResource(resource);
		List<Page> pages = WikipediaResultParser.parseResultToMatchingPages(result);
		return pages;
	}

public static List<PageImageDetails> getImageDetailsRelatedTo(String subjectTitle) {
		ClientResource resource = WikipediaQueries.buildQueryToSearchForImagesAbout(subjectTitle);
		String result = QueryUtils.executeQueryToResource(resource);
		List<PageImageDetails> pageImages = WikipediaResultParser.parseResultToMatchingPageImages(result);
		return pageImages;
	}

}
