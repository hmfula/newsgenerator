package com.coin2012.wikipulse.api;

import java.util.List;

import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.SnippetPage;

/**
 * Defines the business contract of the Wikipulse application.
 * 
 * @author harrison mfula
 * @author karsten packmohr
 * @since 10-11-2012
 */
public interface WikipulseService {
	/**
	 * Fetches news for a given category.
	 * 
	 * @param category
	 *            of news
	 * @return news for a given category
	 */
	public String getNewsForCategory(String category);
	
	/**
	 * Fetches pages for a given category.
	 * 
	 * @param category
	 *            of articles
	 * @return pages for a given category
	 */	
	public String getMostReadTitlesForCategory(String category);
	
	/**
	 * Searches for pages matching given search text
	 * 
	 * @param searchText used during search operation in Wikipedia API
	 * @return a list of page snippet objects (SnippetPage extends Page class) which have matching <code>searchText<code>
	 */
	public List<SnippetPage> searchForPagesThatMatch(String searchText);

	/**
	 * Lists external pages referencing a given page
	 * 
	 * @param url the source url 
	 * @return a list of pages which externally link to url
	 * 
	 */
	public List<Page> searchForPagesReferencing(String url);

}