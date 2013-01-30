package com.coin2012.wikipulse.identification.newsselection;

import java.util.List;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;

import com.coin2012.wikipulse.extraction.neo4j.WikipulseGraphDatabase;
import com.coin2012.wikipulse.models.Page;

public class DomainExperts {

	public static void rankPages(List<Page> pages, int rankId) {
		ExecutionEngine engine = new ExecutionEngine(WikipulseGraphDatabase.getGraphDatabaseServiceInstance());
		
		ExecutionResult authorcount = engine.execute(buildQueryForAllAuthors());
		
		Long authorcountLong = new Long(0);
		
		if (authorcount.columnAs("count").hasNext()) {
			authorcountLong = (Long) authorcount.columnAs("count").next();
		}
		
		for (Page p: pages) {
			ExecutionResult newsauthorcount = engine.execute(buildQueryForPage(p));
			
			if (newsauthorcount.columnAs("count").hasNext()) {
				double d = ((Long) newsauthorcount.columnAs("count").next()).doubleValue();
				double e = authorcountLong.doubleValue();
				
				if (e == 0.0d) {
					p.setRank(rankId, 0);
				} else {
					p.setRank(rankId, d/e);
				}
			}
			
		}
	}
	
	private static String buildQueryForPage(Page p) {
		String querystring = "start n=node:pages(id=\""+p.getPageId()+"\")";
		
		querystring += " MATCH n<-[:EDITED]-a-[:EDITED]->m-[:HAS]->c<-[:HAS]-n";
		querystring += " RETURN COUNT(DISTINCT a) AS count";
		
		return querystring;
	}

	private static String buildQueryForAllAuthors() {
		return "START n=node:authors(\"*:*\") RETURN COUNT (n) AS count";
	}
	
}
