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

}
