package com.coin2012.wikipulse.extraction;

import java.util.List;

import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;

public interface Extractable {

	/**
	 * Returns a list of pages (articles, categories, etc.).
	 * @param category
	 * @return
	 */
	public abstract List<Page> getPagesForCategory(String category);
	
	public List<AggregatedChanges> getRecentChanges(int minChanges);

	public void enhanceNewsWithImages(List<News> news);

	public void enhancePagesWithEdits(List<Page> pages);

	public void enhancePagesWithRelevance(List<Page> pages);
	
	public void saveUserInteraction(String News);
	
	public List<News_Counter> getMostReadNews();
	
	/**
	 * Returns a sorted list of Editors  according to the  number of edits. 
	 * @param editorsNames
	 * @return  a list of Editors in descending order
	 */
	public List<Editor> getWikipediaEditors(List<String> editorsNames);
	
	/**
	 * Tests whether two articles at least one editor in common.
	 * @param firstArticle
	 * @param secondArticle
	 * @return true if two articles at least one editor in common other returns false
	 */
	public boolean doArticlesHaveAtleastOneCommonEditor(Page firstArticle, Page secondArticle);
	
	/**
	 * Summarizes a page
	 * @param url of the  page 
	 * @param length number sentences.  The minimum is 6 sentences.
	 * @return Page object  that contain the summary of the page
	 */
	public Page summarizeArticle(String url, String length);
	
//	/**
//	 * Searches for pages containing search text
//	 * @param searchText
//	 * @return
//	 */
//	public List<SnippetPage> searchForPagesThatMatch(String searchText);
	
//	/**
//	 * Returns pages referencing a particular page (url)
//	 * 
//	 * @param url
//	 * @return
//	 */
//	public List<Page> searchForPagesReferencing(String url);
	
//	/**
//	 * Returns a list of Pages that contain images found in a given page
//	 * @param pageTitle used to search the page for images
//	 * @return page (s) with images
//	 */
//	List<Page> getPageWithImages(String pageTitle);
}