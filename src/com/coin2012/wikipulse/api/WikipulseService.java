package com.coin2012.wikipulse.api;

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
	 *            return string.
	 * @return list of pages with recent changes as JSON string
	 */
	public String getRecentChanges(int minChanges);

	/**
	 * Return a list of news as string in JSON format. The list can be sorted by
	 * either views or time(default) and the amount of News can be set;
	 * 
	 * @param sort
	 *            - either views or time(default)
	 * @param limit
	 *            - amount of News returned
	 * @return list of news as JSON string.
	 */
	public String getNews(String sort, int limit);

	/**
	 * Returns the News for the given newsId as String
	 * 
	 * @param newsId
	 *            - id of the news
	 * @return a News as String
	 */
	public String getNews(String newsId);

	/**
	 * Returns a list of news for a given category as string in JSON format.
	 * 
	 * @param category
	 *            - category of the returned news.
	 * @return list of news as JSON string.
	 */
	public String getNewsForCategory(String category);

	/**
	 * Saves the click of a user on the News with the given id.
	 * 
	 * @param newsId
	 *            - id of the clicked News.
	 */
	public void saveUserInteraction(String newsId);

	/**
	 * Returns a list of categories as string in JSON format. Amount of returned categories is limited.
	 * 
	 * @param limit
	 *            - amount of categories returned
	 * @return list of categories as JSON string.
	 */
	public String getCategories(int limit);
}