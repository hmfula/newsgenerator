package com.coin2012.wikipulse.api;

import java.util.List;

/**
 * Defines the interface for the logic that is offered through the REST routes
 * in the WikipulseResource class.
 * 
 * 
 */
public interface WikipulseService {

	/**
	 * Returns a list of pages with changes in the last 2h as string in JSON
	 * format.
	 * 
	 * @param minChanges
	 *            - amount of changes necessary for a page to added to the
	 *            return string. Default value is 10.
	 * @return list of pages with recent changes as JSON string-
	 */
	public String getRecentChanges(String minChanges);

	/**
	 * Return a list of news as string in JSON format.
	 * 
	 * @param nprop
	 *            - specifies additional content for the returned news sperated
	 *            by |. img = news includes images
	 *            relevance rating.
	 * @param limit 
	 * @return list of news as JSON string.
	 */
	public String getNews(String sort, String limit);

	public String getNews(String newsId);

	/**
	 * Returns a list of news for a given category as string in JSON format.
	 * 
	 * @param category
	 *            - category of the returned news.
	 * @return list of news as JSON string.
	 */
	public String getNewsForCategory(String category);

	public void saveUserInteraction(String News);

	/**
	 * Return a list of categories as string in JSON format.
	 * 
	 * @param nprop
	 *            - currently not used
	 * @return list of categories as JSON string.
	 */
	public String getCategories(String nprop);	
	
	//TODO do we need this here?
	/**
	 * This method returns edit information of Wikipedians.
	 * @param editorNames a list of Wikipedians user names which you would like to query
	 * @return JSON string of editor information
	 */
	public String getEditors(List<String> editorNames);

	//TODO do we need this here?
	/**
	 * Summarizes a page
	 * @param url of the  page 
	 * @param length number sentences.  The minimum is 6 sentences.
	 * @return JSON string  that contain the summary of the page
	 */
	public String  summarizeArticle(String url, String length);
}