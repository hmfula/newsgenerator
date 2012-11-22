package com.coin2012.wikipulse.api;



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
	 * @return page snippets (SnippetPage extends Page class) which contain matching <code>searchText<code>
	 */
	public String searchForPagesThatMatch(String searchText);

	/**
	 * Lists external pages referencing a given page
	 * 
	 * @param url the source url 
	 * @return pages which externally link to url
	 * 
	 */
	public String searchForPagesReferencing(String url);

}