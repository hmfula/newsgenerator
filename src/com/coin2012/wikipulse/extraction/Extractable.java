package com.coin2012.wikipulse.extraction;

import java.util.List;

import com.coin2012.wikipulse.identification.PageImageDetails;
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
	 * Returns a list of ImageDetails objects that encapsulate details of images related to a given topic
	 * @param subjectTitle use to search for related images details
	 * @return a list of image details
	 */
	List<PageImageDetails> getImageDetailsRelatedTo(String subjectTitle);
}