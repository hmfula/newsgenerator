package com.coin2012.wikipulse.models;

import java.util.ArrayList;
import java.util.List;

public class News {

	private final String BASE_URL = "http://en.wikipedia.org/wiki/";

	private String pageId = "";

	private String pageTitle = "";

	private String pageURL = "";

	private String news = "";

	private List<String> imageUrlList = new ArrayList<String>();

	private float yesterdaysRelevance = 0;
	private int viewCount;

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getPagetTitle() {
		return pageTitle;
	}

	public void setPagetTitle(String pagetTitle) {
		this.pageURL = BASE_URL + pagetTitle;
		this.pageTitle = pagetTitle;
	}

	public String getPageLink() {
		return pageURL;
	}

	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
	}

	public String getBASE_URL() {
		return BASE_URL;
	}

	public List<String> getImageUrlList() {
		return imageUrlList;
	}

	public void setRelYesterday(float relevance) {
		if (!Float.isNaN(relevance)) {
			this.yesterdaysRelevance = relevance;
		}
	}

	public float getRelYesterday() {
		return this.yesterdaysRelevance;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

}
