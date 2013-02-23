package com.coin2012.wikipulse.extraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.extraction.hsqldb.AggregatedChanges;
import com.coin2012.wikipulse.extraction.hsqldb.HsqldbManager;
import com.coin2012.wikipulse.extraction.neo4j.ObjectRetriever;
import com.coin2012.wikipulse.extraction.neo4j.ObjectSaver;
import com.coin2012.wikipulse.extraction.smmry.PageSummarizer;
import com.coin2012.wikipulse.extraction.statsgrok.StatsGrokExtractor;
import com.coin2012.wikipulse.extraction.wikipedia.WikipediaExtractor;
import com.coin2012.wikipulse.identification.Timespan;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.PageSummary;
import com.coin2012.wikipulse.models.ShortNews;
import com.coin2012.wikipulse.models.WikiEdit;

/**
 * Implementation of the Extractor interface.
 */
public class ExtractorImpl implements Extractor {

	Logger logger = Logger.getLogger(ExtractorImpl.class.toString());

	private ObjectSaver saver = new ObjectSaver();
	private ObjectRetriever retriever = new ObjectRetriever();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.extraction.Extractor#getPagesForIdentification
	 * (com.coin2012.wikipulse.identification.Timespan, int)
	 */
	@Override
	public List<Page> getPagesForIdentification(Timespan timespan, int minChanges) {
		logger.info("Retrieving pages for identification with at least " + minChanges + " changes.");
		//Getting pageIds from AggregatedChanges with minimum amount of changes
		List<AggregatedChanges> recentChanges = this.getRecentChangesForTimespan(minChanges, timespan);
		List<String> pageids = new ArrayList<String>();
		for (AggregatedChanges aggregatedChanges : recentChanges) {
			pageids.add(aggregatedChanges.getPageid());
		}
		//Getting Pages for pageIds
		List<Page> pages = WikipediaExtractor.getPagesWithCategoriesForPageIds(pageids);
		for (Page page : pages) {
			for (AggregatedChanges aggregated : recentChanges) {
				if (aggregated.getPageid().equals(page.getPageId())) {
					page.setRecentChanges(aggregated.getCount());
					break;
				}
			}
		}
		//updating Pages with Edits
		WikipediaExtractor.updatePagesWithEditsInTimespan(pages, timespan);
		List<Page> crapList = new ArrayList<Page>();
		for (Page page : pages) {
			if (page.getEdits().size() == 0) {
				crapList.add(page);
			}
		}
		for (Page page : crapList) {
			pages.remove(page);
			logger.info("Removing page:" + page.getPageId());
		}
		timespan.setStart(timespan.getEnd());
		return pages;
	}

	private List<AggregatedChanges> getRecentChangesForTimespan(int minChanges, Timespan timer) {
		HashMap<String, AggregatedChanges> map = HsqldbManager.getAllAggregatedChangesFromMemDB(timer);
		List<AggregatedChanges> aggregatedChanges = cleanUpAggregatedChanges(map, minChanges);
		this.sortAggregatedChanges(aggregatedChanges);
		return aggregatedChanges;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.extraction.Extractor#enhanceEditsWithContent(java
	 * .util.List)
	 */
	@Override
	public void enhanceEditsWithContent(List<WikiEdit> edits) {
		WikipediaExtractor.updateEditsWithContent(edits);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.extraction.Extractor#enhancePagesWithRelevance
	 * (java.util.List)
	 */
	@Override
	public void enhancePagesWithRelevance(List<Page> pages) {
		StatsGrokExtractor.updatePagesWithRelevance(pages);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.extraction.Extractor#enhanceNewsWithImages(java
	 * .util.List)
	 */
	@Override
	public void enhanceNewsWithImages(List<News> news) {
		WikipediaExtractor.enhanceNewsWithImages(news);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coin2012.wikipulse.extraction.Extractor#getRecentChanges(int)
	 */
	@Override
	public List<AggregatedChanges> getRecentChanges(int minChanges) {
		HashMap<String, AggregatedChanges> map = HsqldbManager.getAllAggregatedChangesFromMemDB();
		List<AggregatedChanges> aggregatedChanges = cleanUpAggregatedChanges(map, minChanges);
		this.sortAggregatedChanges(aggregatedChanges);
		return aggregatedChanges;
	}

	/**
	 * Includes only AggregatedChanges with a count greater than one
	 * 
	 * @param map
	 * @return
	 */
	private List<AggregatedChanges> cleanUpAggregatedChanges(HashMap<String, AggregatedChanges> map, int minChanges) {
		List<AggregatedChanges> resultList = new ArrayList<AggregatedChanges>();
		for (String key : map.keySet()) {
			AggregatedChanges aggregatedChanges = map.get(key);
			int count = aggregatedChanges.getCount();
			if (count >= minChanges) {
				resultList.add(aggregatedChanges);
			}
		}
		return resultList;
	}

	/**
	 * Sorts list of AggregatedChanges by counted changes
	 * 
	 * @param aggregatedChanges
	 */
	private void sortAggregatedChanges(List<AggregatedChanges> aggregatedChanges) {
		Collections.sort(aggregatedChanges, new Comparator<AggregatedChanges>() {
			public int compare(AggregatedChanges s1, AggregatedChanges s2) {
				return (new Integer(s1.getCount()).compareTo(new Integer(s2.getCount()))) * -1;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.extraction.Extractor#summarizeArticle(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public PageSummary summarizeArticle(String url, String length) {
		PageSummary pageSummary = PageSummarizer.summarizeArticle(url, length);
		return pageSummary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.extraction.Extractor#savePages(java.util.List)
	 */
	@Override
	public void savePages(List<Page> pages) {
		for (Page page : pages) {
			saver.saveOrUpdatePage(page);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coin2012.wikipulse.extraction.Extractor#saveNews(java.util.List)
	 */
	@Override
	public void saveNews(List<News> newsList) {
		for (News news : newsList) {
			saver.saveNews(news);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.extraction.Extractor#saveUserInteraction(java.
	 * lang.String)
	 */
	@Override
	public void saveUserInteraction(String newsId) {
		saver.updateViewCount(newsId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coin2012.wikipulse.extraction.Extractor#getMostViewedNews(int)
	 */
	@Override
	public List<ShortNews> getMostViewedNews(int limit) {
		return retriever.getMostViewedNews(limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coin2012.wikipulse.extraction.Extractor#getLatestNews(int)
	 */
	@Override
	public List<ShortNews> getLatestNews(int limit) {
		return retriever.getLatetestNews(limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.extraction.Extractor#getNews(java.lang.String)
	 */
	@Override
	public News getNews(String newsId) {
		return retriever.getSingleNews(newsId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.extraction.Extractor#getNewsForCategory(java.lang
	 * .String)
	 */
	@Override
	public List<ShortNews> getNewsForCategory(String title) {
		Category category = new Category();
		category.setTitle(title);
		return retriever.getNewsByCategory(category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coin2012.wikipulse.extraction.Extractor#getCategories(int)
	 */
	@Override
	public List<Category> getCategories(int limit) {
		return retriever.getCategoriesWithHighestNewsCount(limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.extraction.Extractor#getDomainExperts(com.coin2012
	 * .wikipulse.models.Category, int)
	 */
	@Override
	public List<Editor> getDomainExperts(Category category, int minEditsInCategory) {
		List<Editor> editors = retriever.getDomainExperts(category, minEditsInCategory);
		return editors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coin2012.wikipulse.extraction.Extractor#getTopContributors(int)
	 */
	@Override
	public List<Editor> getTopContributors(int limit) {
		List<Editor> editors = retriever.getTopEditors(limit);
		return editors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coin2012.wikipulse.extraction.Extractor#getNewsContributors(int)
	 */
	@Override
	public List<Editor> getNewsContributors(int minNews) {
		List<Editor> editors = retriever.getNewsContributors(minNews);
		return editors;
	}
}
