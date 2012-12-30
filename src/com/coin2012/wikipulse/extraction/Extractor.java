package com.coin2012.wikipulse.extraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.coin2012.wikipulse.extraction.hsqldb.HsqldbManager;
import com.coin2012.wikipulse.extraction.statsgrok.StatsGrokExtractor;
import com.coin2012.wikipulse.extraction.wikipedia.WikipediaExtractor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;

/**
 * Represents an Extractor component used to interact with Wikipedia API.
 * 
 * @author Karsten
 * 
 */
public class Extractor implements Extractable {

	@Override
	public List<AggregatedChanges> getRecentChanges(int minChanges) {
		List<AggregatedChanges> aggregatedChanges = this.aggregateChangesFromMemDB(minChanges);
		this.sortAggregatedChanges(aggregatedChanges);
		return aggregatedChanges;
		/*
		 * read last entry from db clear db check if within 2h if then call get
		 * recentChanges(lasttimestamp) else call get recentChanges(2h
		 * timestamp) add result to db aggregate return
		 */
	}

	@Override
	public List<Page> getPagesForCategory(String category) {
		List<Page> pages = WikipediaExtractor.getPagesForCategory(category);
		return pages;
	}
	
	@Override
	public void enhancePagesWithEdits(List<Page> pages){
		WikipediaExtractor.updatePagesWithEdits(pages);
	}
	
	@Override
	public void enhancePagesWithRelevance(List<Page> pages){
		StatsGrokExtractor.updatePagesWithRelevance(pages);
	}
	
	@Override
	public void enhanceNewsWithImages(List<News> news) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<News_Counter> getMostReadNews() {
		List<News_Counter> top10List = HsqldbManager.getMostReadNews();
		return top10List;
	}
	@Override 
	public void saveUserInteraction(String News){
		HsqldbManager.saveUserInteractionInDB(News);
	}
	

//	@Override
//	public List<SnippetPage> searchForPagesThatMatch(String searchText) {
//		List<SnippetPage> pages = WikipediaExtractor.searchForPagesThatMatch(searchText);
//		return pages;
//
//	}
//
//	@Override
//	public List<Page> searchForPagesReferencing(String url) {
//		List<Page> pages = WikipediaExtractor.searchForPagesReferencing(url);
//		return pages;
//
//	}
//
//	@Override
//	public List<Page> getPageWithImages(String pageTitle) {
//		return WikipediaExtractor.getPageWithImages(pageTitle);
//	}

	/**
	 * Counts the changes for each title and returns a list of AggregatedChanges
	 * @return list of AggregatedChanges
	 */
	private List<AggregatedChanges> aggregateChangesFromMemDB(int minChanges) {
		HashMap<String, AggregatedChanges> map = HsqldbManager.getAllAggregatedChangesFromMemDB();
		List<AggregatedChanges> resultList = cleanUpAggregatedChanges(map,minChanges);
		return resultList;
	}

	/**
	 * Includes only AggregatedChanges with a count greater than one
	 * @param map
	 * @return
	 */
	private List<AggregatedChanges> cleanUpAggregatedChanges(HashMap<String, AggregatedChanges> map, int minChanges) {
		List<AggregatedChanges> resultList = new ArrayList<AggregatedChanges>();
		for (String key : map.keySet()) {
			AggregatedChanges aggregatedChanges = map.get(key);
			int count = aggregatedChanges.getCount();
			if(count>= minChanges){
				resultList.add(aggregatedChanges);
			}
		}
		return resultList;
	}
	
	/**
	 * Sorts list of AggregatedChanges by counted changes
	 * @param aggregatedChanges
	 */
	private void sortAggregatedChanges(List<AggregatedChanges> aggregatedChanges) {
		Collections.sort(aggregatedChanges, new Comparator<AggregatedChanges>() {
		    public int compare(AggregatedChanges s1, AggregatedChanges s2) {
		        return (new Integer(s1.getCount()).compareTo(new Integer(s2.getCount()))) * -1;
		    }
		});
	}


}
