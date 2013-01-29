package com.coin2012.wikipulse.extraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.coin2012.wikipulse.extraction.hsqldb.AggregatedChanges;
import com.coin2012.wikipulse.extraction.hsqldb.HsqldbManager;
import com.coin2012.wikipulse.extraction.neo4j.ObjectRetriever;
import com.coin2012.wikipulse.extraction.neo4j.ObjectSaver;
import com.coin2012.wikipulse.extraction.smmry.PageSummarizer;
import com.coin2012.wikipulse.extraction.statsgrok.StatsGrokExtractor;
import com.coin2012.wikipulse.extraction.wikipedia.WikipediaExtractor;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.PageSummary;
import com.coin2012.wikipulse.models.ShortNews;
import com.coin2012.wikipulse.models.WikiEdit;

/**
 * Represents an Extractor component used to interact with Wikipedia API.
 * 
 * @author Karsten
 * 
 */
public class Extractor implements Extractable {

	private ObjectSaver saver = new ObjectSaver();
	private ObjectRetriever retriever = new ObjectRetriever();

	//TODO needed?
	@Override
	public List<Page> getPagesForCategory(String category) {
		List<Page> pages = WikipediaExtractor.getPagesForCategory(category);
		return pages;
	}

	@Override
	public List<Page> getPagesForIdentification() {
		List<AggregatedChanges> recentChanges = this.getRecentChanges(10);
		List<String> pageids = new ArrayList<String>();
		for (AggregatedChanges aggregatedChanges : recentChanges) {
			pageids.add(aggregatedChanges.getPageid());
		}
		List<Page> pages = WikipediaExtractor.getPagesWithCategoriesForPageIds(pageids);
		WikipediaExtractor.updatePagesWithEditsFromTheLastTwoHours(pages);
		return pages;
	}

	@Override
	public void enhanceEditsWithContent(List<WikiEdit> edits) {
		WikipediaExtractor.updateEditsWithContent(edits);
	}

	@Override
	public void enhancePagesWithRelevance(List<Page> pages) {
		StatsGrokExtractor.updatePagesWithRelevance(pages);
	}

	@Override
	public void enhanceNewsWithImages(List<News> news) {
		WikipediaExtractor.enhanceNewsWithImages(news);
	}

	@Override
	public List<AggregatedChanges> getRecentChanges(int minChanges) {
		List<AggregatedChanges> aggregatedChanges = this.aggregateChangesFromMemDB(minChanges);
		this.sortAggregatedChanges(aggregatedChanges);
		return aggregatedChanges;
	}
	
	

	/**
	 * Counts the changes for each title and returns a list of AggregatedChanges
	 * 
	 * @return list of AggregatedChanges
	 */
	private List<AggregatedChanges> aggregateChangesFromMemDB(int minChanges) {
		HashMap<String, AggregatedChanges> map = HsqldbManager.getAllAggregatedChangesFromMemDB();
		List<AggregatedChanges> resultList = cleanUpAggregatedChanges(map, minChanges);
		return resultList;
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

	// TODO move to Identification?
	public boolean doArticlesHaveAtleastOneCommonEditor(Page firstArticle, Page secondArticle) {

		boolean doArticlesHaveAtleastOneCommonEditor = false;
		List<String> users = new ArrayList<String>();
		List<WikiEdit> firstArticleEdits = firstArticle.getEdits();

		List<WikiEdit> secondArticleEdits = secondArticle.getEdits();
		for (WikiEdit wikiedit : secondArticleEdits) {
			users.add(wikiedit.getUser());
		}

		for (WikiEdit currentEdit : firstArticleEdits) {
			if (users.contains(currentEdit.getUser())) {
				doArticlesHaveAtleastOneCommonEditor = true;
				break;
			}

		}
		return doArticlesHaveAtleastOneCommonEditor;
	}

	@Override
	public PageSummary summarizeArticle(String url, String length) {
		PageSummary pageSummary = PageSummarizer.summarizeArticle(url, length);
		return pageSummary;
	}

	@Override
	public void savePages(List<Page> pages) {
		for (Page page : pages) {
			saver.saveOrUpdatePage(page);
		}
	}

	@Override
	public void saveAuthor(Editor editor) {
		saver.saveAuthor(editor);
	}

	@Override
	public void saveNews(List<News> newsList) {
		for (News news : newsList) {
			saver.saveNews(news);
		}
	}

	@Override
	public void saveUserInteraction(String newsId) {
		saver.updateViewCount(newsId);
	}

	@Override
	public List<ShortNews> getMostViewedNews(int limit) {
		return retriever.getMostViewedNews(limit);
	}

	@Override
	public List<ShortNews> getLatestNews(int limit) {
		return retriever.getLatetestNews(limit);
	}

	@Override
	public News getNews(String newsId) {
		return retriever.getSingleNews(newsId);
	}

	@Override
	public List<ShortNews> getNewsForCategory(String title) {
		Category category = new Category();
		category.setTitle(title);
		return retriever.getNewsByCategory(category);
	}

	@Override
	public List<Category> getCategories(int limit) {
		return retriever.getCategoriesWithHighestNewsCount(limit);
	}
	
	@Override
	public List<Editor> getDomainExperts(Category category, int minEditsInCategory){
		List<Editor> editors =retriever.getDomainExperts(category, minEditsInCategory);
		return editors;
	}
	
	@Override
	public List<Editor> getTopContributors(int limit){
		List<Editor> editors =retriever.getTopEditors(limit);
		return editors;
	}
	
	@Override
	public List<Editor> getNewsContributors(int minNews){
		List<Editor> editors =retriever.getNewsContributors(minNews);
		return editors;
	}
}
