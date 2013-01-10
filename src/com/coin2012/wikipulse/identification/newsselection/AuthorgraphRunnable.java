package com.coin2012.wikipulse.identification.newsselection;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class AuthorgraphRunnable implements Runnable {

	private Logger logger = Logger.getLogger("AuthorgraphRunnable.class");
	private Extractable ex;
	
	
	public AuthorgraphRunnable(Extractable extractor) {
		ex = extractor;
	}

	@Override
	public void run() {
		logger.info("Starting authorgraph-database updater");
		
		while (true) {
			
			List<Page> pages = ex.getPagesForCategory(""); // TODO!!!
			ex.enhancePagesWithEdits(pages);
			
			Transaction tx = AuthorgraphDatabase.getGraphDatabaseServiceInstance().beginTx();
			try {
				for (Page p : pages) {
					Node pagenode = AuthorgraphDatabase.getOrCreateNodeWithUniqueFactory(p.getPageId(), "pages");
					pagenode.setProperty("category", p.getCategory());
					
					for (WikiEdit we : p.getEdits()) {
					    long wikiEditTimestamp = (long) 0;
						long lastUpdatedTimestamp = ((Long) pagenode.getProperty("timestamp", 0)).longValue();

					    DateFormat df = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss'Z'");
					    // Wikipedia Date format "2012-12-22T12:00:17Z"
						try {
							Date result = df.parse(we.getTimestamp());
							wikiEditTimestamp = result.getTime();
						} catch (ParseException e) {
							logger.severe("Can not parse date '"+we.getTimestamp()+"'!");
							e.printStackTrace();
						}  
						
						if ( wikiEditTimestamp > lastUpdatedTimestamp) {
							logger.info("Updating node "+pagenode.getProperty("id").toString());
							
							// Update timestamp
							Date now = new Date();
							pagenode.setProperty("timestamp", now.getTime());
							
							// Get-or-create author-node for this WikiEdit
							Node authornode = AuthorgraphDatabase.getOrCreateNodeWithUniqueFactory(we.getUser(), "authors");
							
							// check if author->page relationship already exists
							Iterable<Relationship> relationships = authornode.getRelationships(AuthorgraphDatabase.RelTypes.EDITED);
							Relationship match = null;
							
							while (relationships.iterator().hasNext()) {
								Relationship rel = relationships.iterator().next();
								String pageid = rel.getEndNode().getProperty("id").toString();
								System.out.println("DB:"+pageid+" Page:"+p.getPageId()); // TODO for testing
								if (p.getPageId().contentEquals(pageid)) {
									match = rel;
								}
							}
							
							// relationship does not exist, create it
							if (match == null) {
								match = authornode.createRelationshipTo( pagenode, AuthorgraphDatabase.RelTypes.EDITED );
								match.setProperty("editcount", 0);
							}
							
							// update edit count
							int editCount = ((Integer) match.getProperty("editcount")).intValue();
							editCount++;
							match.setProperty("editcount", editCount);
						}
					}
			}

				tx.success();
			} finally {
				tx.finish();
			}
			
			try {
				Thread.sleep(120000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
