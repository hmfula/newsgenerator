package com.coin2012.wikipulse.extraction;

public class AggregatedChanges {

	private final String title;
	private final String url;
	private int count;
//TODO remove if possible
	public AggregatedChanges(String title) {
		this.title = title;
		url = "en.wikipedia.org/wiki/" + title;
		count = 1;
	}

	public int getCount() {
		return count;
	}

	public void addToCount() {
		count = count + 1;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

}
