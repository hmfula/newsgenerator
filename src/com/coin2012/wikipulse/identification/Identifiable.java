package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;

public interface Identifiable {
	
	public List<News> getNewsForCategory(String category);
	
	/**
	 * Summarizes a page
	 * @param url of the  page 
	 * @param length number sentences.  The minimum is 6 sentences.
	 * @return Page object  that contain the summary of the page
	 */
	public Page summarizeArticle(String url, String length);
	
}
