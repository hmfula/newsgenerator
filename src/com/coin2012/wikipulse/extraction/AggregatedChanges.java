package com.coin2012.wikipulse.extraction;

public class AggregatedChanges {
	
	private String title;
	private int count;
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public AggregatedChanges(String title) {
		this.setTitle(title);
		count = 1;
	}

	public void addToCount() {
		count = count++;
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
