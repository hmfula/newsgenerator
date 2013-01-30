package com.coin2012.wikipulse.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.coin2012.wikipulse.conf.WikipulseConstants;

public class Page {

	private String pageid;
	private int recentChanges;
	private float yesterdaysRelevance;
	private double[] ranks = new double[3];
	private String ns;
	private String title;
	private String timestamp;
	private List<WikiEdit> edits;
	private List<String> imageUrlList = new ArrayList<String>();
	private List<Category> categories = new ArrayList<Category>();
	private String pageTextContent;
	

	public String getPageId() {
		return pageid;
	}

	public void setPageid(String pageid) {
		this.pageid = pageid;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
	public void setRelYesterday(float relevance) {
		this.yesterdaysRelevance = relevance;
	}
	public float getRelYesterday() {
		return this.yesterdaysRelevance;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<WikiEdit> getEdits() {
		if(edits == null){
			return Collections.emptyList();	
		} else {
			return edits;
		}
	}

	public void setEdits(List<WikiEdit> edits) {
		this.edits = edits;
	}

	public List<String> getImageUrlList() {
		return imageUrlList;
	}


	public double getTotalRank() {
		double totalrank = ( yesterdaysRelevance * WikipulseConstants.RELEVANCE_MODIFIER ); 
		
		totalrank += ( ranks[0] * WikipulseConstants.AUTHORS_WITH_NEWS_MODIFIER );
		totalrank += ( ranks[1] * WikipulseConstants.DOMAIN_EXPERTS_MODIFIER );
		totalrank += ( ranks[2] * WikipulseConstants.COMMON_AUTHROS_MODIFIER );
		
		return totalrank;
	}
	
	/**
	 * @return the ranks
	 */
	public double[] getRanks() {
		return ranks;
	}

	/**
	 * @param ranks the ranks to set
	 */
	public void setRank(int id, double rank) {
		ranks[id] = rank;
	}
	
	public void increaseRank(int id) {
		ranks[id]++;
	}
	



	public List<Category> getCategories() {
		return categories;
	}


	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getPageUrl() {
		return WikipulseConstants.WIKIPEDIA_PAGE_BASE_URL + getTitle();
	}
	
	public String getTextContent() {
		return pageTextContent;
	}
	public void setTextContent(String content) {
		pageTextContent = content;
	}

	public int getRecentChanges() {
		return recentChanges;
	}

	public void setRecentChanges(int recentChanges) {
		this.recentChanges = recentChanges;
	}
}
