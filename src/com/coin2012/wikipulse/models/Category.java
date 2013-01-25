package com.coin2012.wikipulse.models;


public class Category {

		private String title = "";
		private long newsCount = 0;
		
		public Category(){};
		
		public Category(String title, long newsCount){
			this.title = title;
			this.newsCount = newsCount;
		}
		
		public String getTitle() {
			return title;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}

		public long getNewsCount() {
			return newsCount;
		}
}
