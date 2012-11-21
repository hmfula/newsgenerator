package com.coin2012.wikipulse.models;

public class SnippetPage extends Page {

	private String wordCount;
	private String size;
	private String snippet;

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

}
