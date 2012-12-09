package com.coin2012.wikipulse.extraction;

import java.util.List;

import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.SnippetPage;

public interface Extractable {

	/**
	 * Returns a list of titles (articles, categories, etc.) with 5 edits each without content
	 * @param category
	 * @return
	 */
	public abstract List<Page> getTitlesForCategory(String category);
	
	/**
	 * Searches for pages containing search text
	 * @param searchText
	 * @return
	 */
	public List<SnippetPage> searchForPagesThatMatch(String searchText);
	
	/**
	 * Returns pages referencing a particular page (url)
	 * 
	 * @param url
	 * @return
	 */
	public List<Page> searchForPagesReferencing(String url);
	
	/**
	 * Returns a list of Pages that contain images found in a given page
	 * @param pageTitle used to search the page for images
	 * @return page (s) with images
	 */
	List<Page> getPageWithImages(String pageTitle);


	List<AggregatedChanges> getRecentChanges(int minChanges);
}