package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.models.News;

/**
 * Defines the News identification business contract for application..
 *
 */
public interface Identifiable {
	
	/**
	 * This method  gathers a list of identified news items.
	 * @return list of news objects
	 */
	public List<News> getNews();
	
}
