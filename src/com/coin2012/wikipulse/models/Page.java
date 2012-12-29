package com.coin2012.wikipulse.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page {

	private static final String BASE_URL = "http://en.wikipedia.org/wiki/";
	private String pageid;
	private float yesterdaysRelevance;
	private int authorgraphRank;
	private String ns;
	private String title;
	private String timestamp;
	private String category;
	private List<WikiEdit> edits;
	private List<Image> images;
	private List<String> imageUrlList = new ArrayList<String>();
	
	public Page() {
		category = "";
	}
	
	
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
		if(edits == null){
			return Collections.emptyList();	
		} else {
			return edits;
		}
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
			
			imageUrlList.add(url);
		}
		
	}
	public List<String> getUrl() {
		if(imageUrlList.size()==0){
		Collections.emptyList();	
		}
		return imageUrlList;
		
	}

	public int getAuthorgraphRank() {
		return authorgraphRank;
	}

	public void setAuthorgraphRank(int authorgraphRank) {
		this.authorgraphRank = authorgraphRank;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
