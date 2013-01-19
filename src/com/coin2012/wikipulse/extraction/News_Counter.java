package com.coin2012.wikipulse.extraction;

public class News_Counter {
		private final String title;
		private final String url;
		private int count;
//TODO remove if possible
		public News_Counter(String title, Integer count) {
			this.title = title;
			url = "en.wikipedia.org/wiki/" + title;
			this.count = count;
		}

		public int getCount() {
			return count;
		}

		public String getTitle() {
			return title;
		}

		public String getUrl() {
			return url;
		}
}
