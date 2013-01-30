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

public interface Extractable {

	public List<Page> getPagesForIdentification(Timespan timer);

	public void enhanceNewsWithImages(List<News> news);

	public void enhanceEditsWithContent(List<WikiEdit> pages);

	public void enhancePagesWithRelevance(List<Page> pages);
	
	public List<AggregatedChanges> getRecentChanges(int minChanges);
	
	/**
	 * Summarizes a page
	 * @param url of the  page 
	 * @param length number sentences.  The minimum is 6 sentences.
	 * @return PageSummary object  that contain the summary of the page
	 */
	public PageSummary summarizeArticle(String url, String length);

	void savePages(List<Page> pages);

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