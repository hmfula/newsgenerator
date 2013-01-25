package com.coin2012.wikipulse.extraction.neo4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;

import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.ShortNews;

public class ObjectRetriever {

	private GraphDatabaseService graphDB;

	//TODO Add Categories to news Object?
	//TODO complete add Page/Editor info
	public List<ShortNews> getNews() {
		graphDB = AuthorgraphDatabase.getGraphDatabaseServiceInstance();
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START news = node:node(*) WHERE HAS (news.timestamp) RETURN news ORDER BY news.timestamp");
		
		return null;
	}
	
	//TODO complete add Page/Editor info
	public List<ShortNews> getLatetestNews(int amount){
		graphDB = AuthorgraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "amount", amount );
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START news = node(*) WHERE HAS (news.timestamp) RETURN news ORDER BY news.timestamp desc LIMIT {amount}", params);
		
		return null;
	}
	
	//TODO complete add Page/Editor info
	// TODO Bug query is empty
	public List<ShortNews> getMostViewedNews(int amount){
		graphDB = AuthorgraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "amount", amount );
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START news = node(*) WHERE HAS (news.viewCount) RETURN news ORDER BY news.viewCount desc LIMIT {amount}", params);
		
		return null;
	}
	
	//TODO complete add Page/Editor info
	//TODO query wrong
	public List<ShortNews> getNewsByCategory(Category category){
		graphDB = AuthorgraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "title", category.getTitle() );
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START category = node:categories(title={title}) MATCH category<-[:HAS]-page<-[:BASED_ON]-news  RETURN news", params);
		
		return null;
	}
	
	public List<Category> getCategoriesWithHighestNewsCount(int amount){
		graphDB = AuthorgraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "amount", amount );
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START n=node(*) MATCH n<-[:HAS]-page<-[:BASED_ON]-newsItem RETURN distinct n, count (distinct newsItem) AS newsCount ORDER BY newsCount desc LIMIT {amount}", params);
		
		
		return null;
	}
	
	//TODO complete add Page/Editor info
	public News getSingleNews(String id){
		graphDB = AuthorgraphDatabase.getGraphDatabaseServiceInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "id", id );
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		ExecutionResult result = engine.execute( "START n= node:news(id={id}) RETURN n", params);
		
		return null;
	}
	
	
	
}
