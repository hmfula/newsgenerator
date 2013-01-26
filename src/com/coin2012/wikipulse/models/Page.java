package com.coin2012.wikipulse.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page {

	private String pageid;
	private float yesterdaysRelevance;
	private int[] ranks = new int[3];
	private String ns;
	private String title;
	private String timestamp;
	private List<WikiEdit> edits;
	private List<Image> images;
	private List<String> imageUrlList = new ArrayList<String>();
	private List<Category> categories = new ArrayList<Category>();
	

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


	public int getTotalRank() {
		int totalrank = (int) (yesterdaysRelevance * 100); 
		
		for (int i = 0; i < ranks.length; i++) {
			totalrank += ranks[i];
		}
		
		return totalrank;
	}
	
	/**
	 * @return the ranks
	 */
	public int[] getRanks() {
		return ranks;
	}

	/**
	 * @param ranks the ranks to set
	 */
	public void setRank(int id, int rank) {
		ranks[id] = rank;
	}
	
	public void increaseRank(int id) {
		ranks[id]++;
	}
	



	public List<Category> getCategories() {
		return categories;
	}


	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
