package com.coin2012.wikipulse.models;

public class SnippetPage extends Page {

	private String wordCount;
	private String size;
	private String snippet;
	private String urlToFullPage;
	public static final String WIKIPEDIA_BASE_URL= "http://www.wikipedia.org/wiki/";
	
	public String getWordCount() {
		return wordCount;
	}

	public void setWordCount(String wordCount) {
		this.wordCount = wordCount;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSize() {
		return this.size;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getSnippet() {
		return this.snippet;
	}
	
	public void setPageUrl(String pageTitle) {
		if(pageTitle != null && pageTitle.equalsIgnoreCase(getTitle())){
			urlToFullPage= WIKIPEDIA_BASE_URL + pageTitle;
				
		}else{
			urlToFullPage = WIKIPEDIA_BASE_URL + getTitle();	
		}
		
	
	}

	public String getPageUrl() {
		return this.urlToFullPage;
	}

}
