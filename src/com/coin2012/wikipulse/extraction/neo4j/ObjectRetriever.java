package com.coin2012.wikipulse.extraction.neo4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;

import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.News;

public class ObjectRetriever {

	private GraphDatabaseService graphDB;

	public List<News> getNews() {
		graphDB = AuthorgraphDatabase.getGraphDatabaseServiceInstance();
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START news = node:news(*) RETURN n");
		
		return null;
	}
	
	public List<News> getLatetestNews(String amount){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "amount", amount );
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START news = node:news(*) RETURN n ORDER BY n.timestamp desc LIMIT {amount}", params);
		
		return null;
	}
	
	public List<News> getMostViewedNews(String amount){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "amount", amount );
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START news = node:news(*) RETURN n ORDER BY n.viewCount desc LIMIT {amount}", params);
		
		return null;
	}
	
	public List<News> getNewsByCategory(Category category){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "title", category.getTitle() );
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START category = node:categories(title={title}) MATCH category<-[:HAS]-page<-[:BASED_ON]-news  RETURN news", params);
		
		return null;
	}
	
	public List<Category> getCategoriesWithHighestNewsCount(String amount){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "amount", amount );
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START categories = node:categories(*) MATCH categories<-[:HAS]-page<-[:BASED_ON]-news RETURN categories, count (distinct news) AS newsCount ORDER BY newsCount desc LIMIT {amount}", params);
		
		return null;
	}
	
	public News getSingleNews(String id){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "id", id );
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START news = node:news(id={id}) RETURN n", params);
		
		return null;
	}
	
	
	
}
