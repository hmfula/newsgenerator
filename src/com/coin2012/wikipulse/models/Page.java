package com.coin2012.wikipulse.models;

import java.util.List;

public class Page {

	private String pageid;
	private float yesterdaysRelevance;
	private String ns;
	private String title;
	private String timestamp;
	private List<WikiEdit> edits;

	public String getPageId() {
		return pageid;
	}

	public void setPageid(String pageid) {
		this.pageid = pageid;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
	public void setRelYesterday(float relevance) {
		this.yesterdaysRelevance = relevance;
	}
	public float getRelYesterday() {
		return this.yesterdaysRelevance;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<WikiEdit> getEdits() {
		return edits;
	}

	public void setEdits(List<WikiEdit> edits) {
		this.edits = edits;
	}

}
