package com.coin2012.wikipulse.models;

import java.util.List;

public class News extends ShortNews {

	private String news;

	public News() {
		super();
	};

	public News(String id, String shortNews, List<String> imageUrlList, long viewCount, String timestamp) {
		super(id, shortNews, imageUrlList, viewCount, timestamp);
	}

	public News(String id, List<String> imageUrlList, Long viewCount, String timestamp) {
		super(id, imageUrlList, viewCount, timestamp);
	}

	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
		// TODO generate better shortNews;
		shortNews = news.substring(0, Math.min(50, news.length()));
	}
}
