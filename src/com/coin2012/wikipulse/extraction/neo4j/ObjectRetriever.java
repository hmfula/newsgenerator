package com.coin2012.wikipulse.extraction.neo4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.ShortNews;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ObjectRetriever {

	private GraphDatabaseService graphDB;
	private Gson gson = new Gson();

	// TODO Add Categories to news Object?
	public List<ShortNews> getNews() {
		List<ShortNews> shortNews = new ArrayList<ShortNews>(); 
		graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		ExecutionEngine engine = new ExecutionEngine(graphDB);
		ExecutionResult result = engine.execute("START newsItems=node:news('*:*') MATCH (newsItems)-[:BASED_ON]->(page), (newsItems)-[:BASED_ON_EDIT_OF]->(author) RETURN newsItems,page.id,page.title,author ORDER BY newsItems.timestamp");
		for (Map<String, Object> row : result) {
			shortNews.add(this.generateShortNewsFromRow(row));
		}
		return shortNews;
	}


	public List<ShortNews> getLatetestNews(int amount) {
		List<ShortNews> shortNews = new ArrayList<ShortNews>(); 
		graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
		ExecutionEngine engine = new ExecutionEngine(graphDB);
		ExecutionResult result = engine.execute("START newsItems = node:news('*:*') MATCH (newsItems)-[:BASED_ON]->(page), (newsItems)-[:BASED_ON_EDIT_OF]->(author) RETURN newsItems,page.id,page.title,author ORDER BY newsItems.timestamp desc LIMIT {amount}", params);

		for (Map<String, Object> row : result) {
			shortNews.add(this.generateShortNewsFromRow(row));
		}
		return shortNews;
	}

	public List<ShortNews> getMostViewedNews(int amount) {
		List<ShortNews> shortNews = new ArrayList<ShortNews>(); 
		graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
		ExecutionEngine engine = new ExecutionEngine(graphDB);
		ExecutionResult result = engine.execute("START newsItems = node:news('*:*') MATCH (newsItems)-[:BASED_ON]->(page), (newsItems)-[:BASED_ON_EDIT_OF]->(author) WHERE newsItems.viewCount>0 RETURN newsItems,page.id,page.title,author ORDER BY newsItems.viewCount desc LIMIT {amount}", params);

		for (Map<String, Object> row : result) {
			shortNews.add(this.generateShortNewsFromRow(row));
		}
		return shortNews;
	}

	public List<ShortNews> getNewsByCategory(Category category) {
		List<ShortNews> shortNews = new ArrayList<ShortNews>(); 
		graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("title", category.getTitle());
		ExecutionEngine engine = new ExecutionEngine(graphDB);
		ExecutionResult result = engine.execute("START category = node:categories(title={title}) MATCH category<-[:HAS]-page<-[:BASED_ON]-newsItems-[:BASED_ON_EDIT_OF]->author  RETURN newsItems,page.id,page.title,author ORDER BY newsItems.timestamp desc", params);

		for (Map<String, Object> row : result) {
			shortNews.add(this.generateShortNewsFromRow(row));
		}
		return shortNews;
	}

	public List<Category> getCategoriesWithHighestNewsCount(int amount) {
		List<Category> categories = new ArrayList<Category>();
		graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
		ExecutionEngine engine = new ExecutionEngine(graphDB);
		ExecutionResult result = engine.execute("START category=node:categories('*:*') MATCH category<-[:HAS]-page<-[:BASED_ON]-newsItem RETURN distinct category, count (distinct newsItem) AS newsCount ORDER BY newsCount desc LIMIT {amount}",params);
		for (Map<String, Object> row : result) {
			String title = ((Node)row.get("category")).getProperty("title").toString();
			long newsCount = (Long)row.get("newsCount");
			Category category = new Category(title,newsCount);
			categories.add(category);
		}
		
		return categories;
	}

	public News getSingleNews(String id) {
		News news = null;
		graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		ExecutionEngine engine = new ExecutionEngine(graphDB);
		ExecutionResult result = engine.execute("START newsItems = node:news(id={id}) MATCH (newsItems)-[:BASED_ON]->(page), (newsItems)-[:BASED_ON_EDIT_OF]->(author) RETURN newsItems,page.id,page.title,author", params);
		for (Map<String, Object> row : result) {
			news = this.generateNewsFromRow(row);
			break;
		}
		return news;
	}
	
	public List<Editor> getDomainExperts(Category category, int minEditsInCategory){
		List<Editor> editors = new ArrayList<Editor>();
		graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("title", category.getTitle());
		params.put("minEdits", minEditsInCategory);
		ExecutionEngine engine = new ExecutionEngine(graphDB);
		ExecutionResult result = engine.execute("START category = node:categories(title={title}) MATCH category<-[:HAS]-page<-[:EDITED]-author WITH distinct author , count(author) AS domainEdits WHERE domainEdits>={minEdits} RETURN author", params);
		for (Map<String, Object> row : result) {
			Editor author = this.generateAuthorFrom(row);
			editors.add(author);
		}
		return editors;	
	}
	
	public List<Editor> getNewsContributors (int minNews){
		List<Editor> editors = new ArrayList<Editor>();
		graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("minNews", minNews);
		ExecutionEngine engine = new ExecutionEngine(graphDB);
		ExecutionResult result = engine.execute("START author = node:authors('*:*') MATCH ()-[based:BASED_ON_EDIT_OF]->author WITH distinct author , count(based) AS countedNews WHERE countedNews>={minNews} RETURN author", params);
		for (Map<String, Object> row : result) {
			Editor author = this.generateAuthorFrom(row);
			editors.add(author);
		}
		return editors;	
	}
	
	public List<Editor> getTopEditors (int amount){
		List<Editor> editors = new ArrayList<Editor>();
		graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
		ExecutionEngine engine = new ExecutionEngine(graphDB);
		ExecutionResult result = engine.execute("START author = node:authors('*:*') MATCH author-[edits:EDITED]->() RETURN distinct author , count(edits) AS editCount ORDER BY editCount desc LIMIT {amount}", params);
		for (Map<String, Object> row : result) {
			Editor author = this.generateAuthorFrom(row);
			editors.add(author);
		}
		return editors;	
	}

	private Editor generateAuthorFrom(Map<String, Object> row) {
		Node editorNode = (Node)row.get("author");
		String userId = editorNode.getProperty("id").toString();
		String name = editorNode.getProperty("user").toString();
		Editor editor = new Editor(userId, name);
		return editor;
	}


	/**
	 * 
	 * @param row - needs the format newsItem,page.id,page.title,editor
	 * @return
	 */
	private ShortNews generateShortNewsFromRow(Map<String, Object> row) {
		Node newsNode = (Node)row.get("newsItems");
		String id = newsNode.getProperty("id").toString();
		List<String> imageUrlList = gson.fromJson(newsNode.getProperty("imageUrlList").toString(), new TypeToken<List<String>>(){}.getType()) ;
		Long viewCount= (Long)(newsNode.getProperty("viewCount"));
		String timestamp = newsNode.getProperty("timestamp").toString();
		
		String news = newsNode.getProperty("shortNews").toString();
		ShortNews shortNews = new ShortNews(id, news, imageUrlList, viewCount,timestamp);
		
		enhanceNewsWithPageNEditor(row, shortNews);
		
		return shortNews;
	}
	
	/**
	 * 
	 * @param row - needs the format newsItem,page.id,page.title,editor
	 * @return
	 */
	private News generateNewsFromRow(Map<String, Object> row) {
		Node newsNode = (Node)row.get("newsItems");
		String id = newsNode.getProperty("id").toString();
		List<String> imageUrlList = gson.fromJson(newsNode.getProperty("imageUrlList").toString(), new TypeToken<List<String>>(){}.getType()) ;
		Long viewCount= (Long)(newsNode.getProperty("viewCount"));
		String timestamp = newsNode.getProperty("timestamp").toString();
		
		String news = newsNode.getProperty("news").toString();
		News newsItem = new News(id, imageUrlList, viewCount,timestamp);
		newsItem.setNews(news);
		
		
		enhanceNewsWithPageNEditor(row, newsItem);
		
		return newsItem;
	}


	private void enhanceNewsWithPageNEditor(Map<String, Object> row, ShortNews shortNews) {
		shortNews.setPageId(row.get("page.id").toString());
		shortNews.setPagetTitle(row.get("page.title").toString());
		
		Editor editor = this.generateAuthorFrom(row);
		shortNews.setEditor(editor);
	}

}
