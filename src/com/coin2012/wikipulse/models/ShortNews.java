package com.coin2012.wikipulse.models;

import java.util.ArrayList;
import java.util.List;

public class ShortNews {
	private final String BASE_URL = "http://en.wikipedia.org/wiki/";

	private String id;

	protected String shortNews;

	private String timestamp;

	private List<String> imageUrlList = new ArrayList<String>();

	private long viewCount = 0;

	private String pageId = "";

	private String pageTitle = "";

	private String pageURL = BASE_URL + pageTitle;

	private List<Editor> editors;

	public ShortNews(String id, String shortNews, List<String> imageUrlList, long viewCount, String timestamp) {
		this.id = id;
		this.shortNews = shortNews;
		this.imageUrlList = imageUrlList;
		this.viewCount = viewCount;
		this.timestamp = timestamp;
	}

	public ShortNews(String id, List<String> imageUrlList, long viewCount, String timestamp) {
		this.id = id;
		this.imageUrlList = imageUrlList;
		this.viewCount = viewCount;
		this.timestamp = timestamp;
	}

	public ShortNews() {
	}

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

	public List<String> getImageUrlList() {
		return imageUrlList;
	}

	public List<Editor> getEditors() {
		return editors;
	}

	public void setEditor(List<Editor> editors) {
		this.editors = editors;
	}

	public String getId() {
		return id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getShortNews() {
		return shortNews;
	}

	public long getViewCount() {
		return viewCount;
	}
}