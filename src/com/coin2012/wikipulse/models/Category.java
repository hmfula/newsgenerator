package com.coin2012.wikipulse.models;


public class Category {

		private String id = "";
		private String title = "";
		private long newsCount = 0;
		
		public Category(){};
		
		public Category(String title, long newsCount){
			this.id = title.toLowerCase();
			this.title = title;
			this.newsCount = newsCount;
		}
		
		public String getTitle() {
			return title;
		}
		
		public void setTitle(String title) {
			this.title = title;
			this.id  = title.toLowerCase();
		}

		public long getNewsCount() {
			return newsCount;
		}

		public String getId() {
			if(id.equals("")){
				this.id = title.toLowerCase();
			}
			return id;
		}
}
