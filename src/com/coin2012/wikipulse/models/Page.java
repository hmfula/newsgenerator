package com.coin2012.wikipulse.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page {

	private static final String BASE_URL = "http://en.wikipedia.org/wiki/";
	private String pageid;
	private float yesterdaysRelevance;
	private String ns;
	private String title;
	private String timestamp;
	private List<WikiEdit> edits;
	private List<Image> images;
	List<String> imageUrlList = new ArrayList<String>();
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

	public List <Image>getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
	
	public static class Image{
		String ns;
		String title;
		private String url;
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
		
		public void setUrl(String url) {
			this.url = url;
		}
		public String getUrl() {
			return url;
		}
		
	}

	public void setUrl(String url) {
		if(url != null){
			
			imageUrlList.add(BASE_URL+url);
		}
		
	}
	public List<String> getUrl() {
		if(imageUrlList.size()==0){
		Collections.emptyList();	
		}
		return imageUrlList;
		
	}

}
