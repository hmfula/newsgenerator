package com.coin2012.wikipulse.extraction;

import java.util.List;

import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.ShortNews;

public interface Extractable {

	/**
	 * Returns a list of pages (articles, categories, etc.).
	 * @param category
	 * @return
	 */
	public abstract List<Page> getPagesForCategory(String category);
	
	public List<Page> getPagesForIdentification();

	public void enhanceNewsWithImages(List<News> news);

	public void enhancePagesWithEdits(List<Page> pages);

	public void enhancePagesWithRelevance(List<Page> pages);
	
	public List<AggregatedChanges> getRecentChanges(int minChanges);

	//TODO do we need this here?
	/**
	 * Returns a sorted list of Editors  according to the  number of edits. 
	 * @param editorsNames
	 * @return  a list of Editors in descending order
	 */
	public List<Editor> getWikipediaEditors(List<String> editorsNames);
	
	//TODO do we need this here?
	/**
	 * Tests whether two articles at least one editor in common.
	 * @param firstArticle
	 * @param secondArticle
	 * @return true if two articles at least one editor in common other returns false
	 */
	public boolean doArticlesHaveAtleastOneCommonEditor(Page firstArticle, Page secondArticle);

	//TODO do we need this here?
	/**
	 * Summarizes a page
	 * @param url of the  page 
	 * @param length number sentences.  The minimum is 6 sentences.
	 * @return Page object  that contain the summary of the page
	 */
	public Page summarizeArticle(String url, String length);

	void savePages(List<Page> pages);

	void saveAuthor(Editor editor);

	void saveNews(List<News> newsList);

	public void saveUserInteraction(String News);

	List<ShortNews> getMostViewedNews(int limit);

	List<ShortNews> getLatestNews(int limit);

	News getNews(String newsId);

	List<ShortNews> getNewsForCategory(String title);

	List<Category> getCategories(int limit);
}