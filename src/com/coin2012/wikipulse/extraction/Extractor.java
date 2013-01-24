package com.coin2012.wikipulse.extraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.coin2012.wikipulse.extraction.hsqldb.HsqldbManager;
import com.coin2012.wikipulse.extraction.neo4j.ObjectSaver;
import com.coin2012.wikipulse.extraction.smmry.PageSummarizer;
import com.coin2012.wikipulse.extraction.statsgrok.StatsGrokExtractor;
import com.coin2012.wikipulse.extraction.wikipedia.WikipediaExtractor;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

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
	}

	@Override
	public List<Page> getPagesForCategory(String category) {
		List<Page> pages = WikipediaExtractor.getPagesForCategory(category);
		return pages;
	}
	
	@Override
	public List<Page> getPagesForIdentification(){
		List<AggregatedChanges> recentChanges =  this.getRecentChanges(0);
		List<String> pageids = new ArrayList<String>();
		for (AggregatedChanges aggregatedChanges : recentChanges) {
			pageids.add(aggregatedChanges.getPageid());
		}
		List<Page> pages = WikipediaExtractor.getPagesWithCategoriesForPageIds(pageids);
		WikipediaExtractor.updatePagesWithEditsFromTheLastTwoHours(pages);
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
		WikipediaExtractor.enhanceNewsWithImages(news);
	}
	
	@Override
	public List<News> getTop10MostReadNews() {
		//TODO Method to query neo4j for top 10 news

		return new ArrayList<News>();
	}
	@Override 
	public void saveUserInteraction(String newsId){
		//TODO Method to update viewcount for a news over
	}

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

	public List<Editor> getWikipediaEditors(List<String> editorsNames) {
		List<Editor> editors = WikipediaExtractor.getWikipediaEditors(editorsNames);
		Collections.sort(editors, new Comparator<Editor>() {
		    public int compare(Editor currentEditor, Editor nextEditor) {
		        return  Integer.parseInt(nextEditor.getEditcount()) -
		        		Integer.parseInt(currentEditor.getEditcount());
		    }
		});
	return editors;
}
	
	public boolean doArticlesHaveAtleastOneCommonEditor(Page firstArticle, Page secondArticle){
		//TODO move to Identification?
		boolean doArticlesHaveAtleastOneCommonEditor = false;
		List<String> users = new ArrayList<String>();
		List<WikiEdit> firstArticleEdits = firstArticle.getEdits();
		
		List<WikiEdit>secondArticleEdits = secondArticle.getEdits();
		for (WikiEdit wikiedit : secondArticleEdits){
			users.add(wikiedit.getUser());
		}
		
		for (WikiEdit currentEdit : firstArticleEdits) {
			if(users.contains(currentEdit.getUser())){
				doArticlesHaveAtleastOneCommonEditor =  true;	
				break;
			}
			
		}
		return doArticlesHaveAtleastOneCommonEditor;
	}

	
	/**
	 * TODO : Method to be moved to identifier after fixing threading issue
	 *
	 */
	public Page summarizeArticle(String url, String length) {
		Page page = PageSummarizer.summarizeArticle(url, length);
		return page;
	}
	
	@Override
	public void savePages(List<Page> pages){
		ObjectSaver saver = new ObjectSaver();
		for (Page page : pages) {
			saver.saveOrUpdatePage(page);
		}
	}
	
	@Override
	public void saveAuthor(Editor editor){
		ObjectSaver saver = new ObjectSaver();
		saver.saveAuthor(editor);
	}
	
	@Override
	public void saveNews(List<News> newsList){
		ObjectSaver saver = new ObjectSaver();
		for (News news : newsList) {
			saver.saveNews(news);
		}
	}
}
