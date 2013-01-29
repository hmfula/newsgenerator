package com.coin2012.wikipulse.identification.newsselection;

import java.util.Iterator;
import java.util.List;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;

import com.coin2012.wikipulse.extraction.neo4j.WikipulseGraphDatabase;
import com.coin2012.wikipulse.models.Page;

public class CommonAuthors {

	public static void rankPages(List<Page> pages, int rankId) {
		ExecutionEngine engine = new ExecutionEngine(WikipulseGraphDatabase.getGraphDatabaseServiceInstance());
		
		for (Page p: pages) {
			ExecutionResult authors = engine.execute(buildQuery(p));
			Iterator<Long> it = authors.columnAs("authorcount");
			
			if (it.hasNext()) {
				Long l = it.next();
				
				//System.out.println("query: "+buildQuery(p)+" -  result: "+l);
				p.setRank(rankId, l.intValue());
			}
			
		}
	}
	
	private static String buildQuery(Page p) {
		String querystring = "start n=node:pages(id=\""+p.getPageId()+"\")";
		
		querystring += " MATCH n<-[:EDITED]-a-[:EDITED]->m-[:HAS]->c<-[:HAS]-n";
		querystring += " RETURN COUNT(a) AS authorcount";
		
		return querystring;
	}
	
}
