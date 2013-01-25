package com.coin2012.wikipulse.models;


public class News extends ShortNews{

	private String news;

	//TODO Remove? or do we need this?
	private float yesterdaysRelevance = 0;


	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
		//TODO generate shortNews;
		shortNews = news;
	}


	public void setRelYesterday(float relevance) {
		if (!Float.isNaN(relevance)) {
			this.yesterdaysRelevance = relevance;
		}
	}

	public float getRelYesterday() {
		return this.yesterdaysRelevance;
	}
}
