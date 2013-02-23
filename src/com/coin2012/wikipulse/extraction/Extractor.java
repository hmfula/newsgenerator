package com.coin2012.wikipulse.extraction;

import java.util.List;

import com.coin2012.wikipulse.extraction.hsqldb.AggregatedChanges;
import com.coin2012.wikipulse.identification.Timespan;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.PageSummary;
import com.coin2012.wikipulse.models.ShortNews;
import com.coin2012.wikipulse.models.WikiEdit;
/**
 * Handles the extraction of data from external sources and handles the interaction with databases. 
 */
public interface Extractor {

	/**
	 * Returns a list of pages with edits that have at least the given amount of changes in the specified timespan.  
	 * @param timer - timespan
	 * @param minChanges - amount of changes
	 * @return list of pages
	 */
	public List<Page> getPagesForIdentification(Timespan timer, int minChanges);

	/**
	 * Enhances each news, if possible, with images from the page on which the news is based.
	 * @param news - list of news to be enhanced
	 */
	public void enhanceNewsWithImages(List<News> news);

	/**
	 * Enhances each edit with the content of the edit on wikipedia.
	 * @param edits - list of edits to be enhanced
	 */
	public void enhanceEditsWithContent(List<WikiEdit> edits);

	/**
	 * Enhances each page with a relevance value based on the yesterday views in relation to the views of the last 30 days. Extracted from statsgrok.
	 * @param pages - list of pages to be enhanced
	 */
	public void enhancePagesWithRelevance(List<Page> pages);
	
	/**
	 * Returns a list of AggregatedChanges that have at least the given amount of changes from the in-memory db.
	 * @param minChanges - amount of changes
	 * @return list of AggregatedChanges
	 */
	public List<AggregatedChanges> getRecentChanges(int minChanges);
	
	//TODO  remove or fix
	/**
	 * Summarizes a page
	 * @param url of the  page 
	 * @param length number sentences.  The minimum is 6 sentences.
	 * @return PageSummary object  that contain the summary of the page
	 */
	public PageSummary summarizeArticle(String url, String length);

	/**
	 * Saves a list of pages in the graph database.
	 * @param pages - list of pages
	 */
	public void savePages(List<Page> pages);

	/**
	 * Saves a list of pages in the graph database.
	 * @param newsList - list of news
	 */
	public void saveNews(List<News> newsList);

	/**
	 * Updates the click count for the news with the given id in the graph database.
	 * @param newsId - id of the news
	 */
	public void saveUserInteraction(String newsId);

	/**
	 * Returns the list of most viewed news from the graph db. Limited by the given amount. 
	 * @param limit - maximum amount
	 * @return list of most viewed news
	 */
	public List<ShortNews> getMostViewedNews(int limit);

	/**
	 * Returns the list of latest news from the graph db. Limited by the given amount. 
	 * @param limit - maximum amount
	 * @return list of latest news
	 */
	public List<ShortNews> getLatestNews(int limit);

	/**
	 * Returns the news for the given id from the graph db.
	 * @param newsId - id of the news
	 * @return the news
	 */
	public News getNews(String newsId);

	/**
	 * Returns a list of news for the given category from the graph db.
	 * @param category - category of the news
	 * @return list of news
	 */
	public List<ShortNews> getNewsForCategory(String category);

	/**
	 * Returns a list of categories from the graph db. The amount is limited by the given value.
	 * @param limit - maximum amount
	 * @return list of categories
	 */
	public List<Category> getCategories(int limit);

	/**
	 * Returns a list of editors that did at least the given amount of edits in the given category from the graph db.
	 * @param category - category
	 * @param minEditsInCategory - minimum edits in category
	 * @return list of editors
	 */
	public List<Editor> getDomainExperts(Category category, int minEditsInCategory);

	/**
	 * Returns the list of the editors with the most edits from the graph db. The given limit sets the maximum of how many editors are returned.
	 * @param limit -  maximum amount of editors
	 * @return list of editors
	 */
	public List<Editor> getTopContributors(int limit);

	/**
	 * Returns the list of editors with at least the given amount of news from the graph db.
	 * @param minNews - minimum amount of news
	 * @return list of editors
	 */
	public List<Editor> getNewsContributors(int minNews);
}