package com.coin2012.wikipulse.models;

import java.util.ArrayList;
import java.util.List;

public class News {

	private final String BASE_URL = "http://en.wikipedia.org/wiki/";
	private String pageId;
	private String pagetTitle;
	private String pageURL;
	private String news;
	private List<String> imageUrlList = new ArrayList<String>();

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getPagetTitle() {
		return pagetTitle;
	}

	public void setPagetTitle(String pagetTitle) {
		this.pageURL = BASE_URL + pagetTitle;
		this.pagetTitle = pagetTitle;
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
}
