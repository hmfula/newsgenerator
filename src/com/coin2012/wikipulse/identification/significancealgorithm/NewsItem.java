package com.coin2012.wikipulse.identification.significancealgorithm;

import com.coin2012.wikipulse.models.Page;

/**
 * 
 * This class represents a news item that will be presented to the end user.
 * 
 * @author Stefan
 *
 */
public class NewsItem implements Comparable<NewsItem>{

	private Page page;
	private int rank;
	
	
	public NewsItem(Page page) {
		this.page = page;
		rank = 0;
	}

	@Override
	public int compareTo(NewsItem arg0) {
		return rank - arg0.getRank();
	}
	
	public void increaseRank() {
		rank++;
	}
	
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}


}
