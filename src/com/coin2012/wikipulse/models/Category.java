package com.coin2012.wikipulse.models;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Category {

		private String categoryId = "";
		private String categoryTitle = "";
		private int numberOfNews = 0;
		private String dateCreated = "";
		private String dateUpdated = "";
		
		public Category(String categoryID, String categoryTitle){
			this.categoryId = categoryID;
			this.categoryTitle = categoryTitle;
			setDateCreated();
			setDateUpdated();
		}
		
		public Category(String categoryID, String categoryTitle, String dateCreated, String dateUpdated){
			this.categoryId = categoryID;
			this.categoryTitle = categoryTitle;
			this.dateCreated = dateCreated;
			this.dateUpdated = dateUpdated;
		}

		public String getCategoryID() {
			return categoryId;
		}

		public void setCategoryID(String categoryId) {
			this.categoryId = categoryId;
		}

		public String getCategoryTitle() {
			return categoryTitle;
		}

		public void setCategoryTitle(String categoryTitle) {
			this.categoryTitle = categoryTitle;
			setDateUpdated();
		}

		public String getDateUpdated() {
			return dateUpdated;
		}

		public String getDateCreated() {
			return dateCreated;
		}

		public void setNumberOfNews(int no) {
			setDateUpdated();
			numberOfNews = no;
		}

		public int getNumberOfNews() {
			return numberOfNews;
		}
		public void setDateUpdated(){
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			this.dateUpdated = dateFormat.format(date);
		}
		private void setDateCreated(){
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			this.dateCreated = dateFormat.format(date);
		}
}
