package com.coin2012.wikipulse.karsten.demo;

import java.util.List;

public class Title {

	private String pageid;
	private String ns;
	private String title;
	private String timestamp;
	private List<Edit> edits;

	public String getPageid() {
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

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<Edit> getEdits() {
		return edits;
	}

	public void setEdits(List<Edit> edits) {
		this.edits = edits;
	}

}
