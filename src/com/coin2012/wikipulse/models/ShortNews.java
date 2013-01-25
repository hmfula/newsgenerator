package com.coin2012.wikipulse.models;

import java.util.ArrayList;
import java.util.List;

public class ShortNews {
	private final String BASE_URL = "http://en.wikipedia.org/wiki/";

	private String id;
	
	protected String shortNews;
	
	private String timestamp;
	
	private List<String> imageUrlList = new ArrayList<String>();
	
	private long viewCount=0;
	
	
	private String pageId = "";

	private String pageTitle = "";

	private String pageURL = BASE_URL + pageTitle;

	
	private Editor editor;
	
	
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

	public Editor getEditor() {
		return editor;
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
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