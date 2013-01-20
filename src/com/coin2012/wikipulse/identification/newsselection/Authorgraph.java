package com.coin2012.wikipulse.identification.newsselection;

import java.util.Iterator;
import java.util.List;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.identification.IdentificationRunnable;
import com.coin2012.wikipulse.models.Page;

public class Authorgraph {
	
	private static Thread authorgraphThread;

	public static void initAuthorgraphDatabaseThread(Extractable extractor) {
		if (authorgraphThread == null ) {
			authorgraphThread = new Thread(new IdentificationRunnable(extractor));
			authorgraphThread.run();
		}
	}
	
	
	public static void rankPages(List<Page> pages) {
		ExecutionEngine engine = new ExecutionEngine(AuthorgraphDatabase.getGraphDatabaseServiceInstance());
		
		for (Page p: pages) {
			//simple, not jet final version
			ExecutionResult otherauthors = engine.execute( "start n=node:pages(key="+p.getPageId()+") MATCH n<-[:EDITED]-(a)-[:EDITED]->op WHERE op.category = "+p.getCategory()+" RETURN COUNT(a) as authorcount");

			Iterator<Integer> it = otherauthors.columnAs("authorcount");
			
			if (it.hasNext()) {
				p.setAuthorgraphRank(it.next());
			}
			
		}
	}
}
