package com.coin2012.wikipulse.extraction;

import java.util.List;

import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.PageSummary;
import com.coin2012.wikipulse.models.ShortNews;
import com.coin2012.wikipulse.models.WikiEdit;

public interface Extractable {

	/**
	 * Returns a list of pages (articles, categories, etc.).
	 * @param category
	 * @return
	 */
	public abstract List<Page> getPagesForCategory(String category);
	
	public List<Page> getPagesForIdentification();

	public void enhanceNewsWithImages(List<News> news);

	public void enhanceEditsWithContent(List<WikiEdit> pages);

	public void enhancePagesWithRelevance(List<Page> pages);
	
	public List<AggregatedChanges> getRecentChanges(int minChanges);
	
	//TODO do we need this here?
	/**
	 * Tests whether two articles at least one editor in common.
	 * @param firstArticle
	 * @param secondArticle
	 * @return true if two articles at least one editor in common other returns false
	 */
	public boolean doArticlesHaveAtleastOneCommonEditor(Page firstArticle, Page secondArticle);

	/**
	 * Summarizes a page
	 * @param url of the  page 
	 * @param length number sentences.  The minimum is 6 sentences.
	 * @return PageSummary object  that contain the summary of the page
	 */
	public PageSummary summarizeArticle(String url, String length);

	void savePages(List<Page> pages);

	void saveAuthor(Editor editor);

	void saveNews(List<News> newsList);

	public void saveUserInteraction(String News);

	List<ShortNews> getMostViewedNews(int limit);

	List<ShortNews> getLatestNews(int limit);

	News getNews(String newsId);

	List<ShortNews> getNewsForCategory(String title);

	List<Category> getCategories(int limit);

	List<Editor> getDomainExperts(Category category, int minEditsInCategory);

	List<Editor> getTopContributors(int limit);

	List<Editor> getNewsContributors(int minNews);
}