package com.coin2012.wikipulse.identification.newsselection;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.UniqueFactory;

import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class Authorgraph {
	
	private static Logger logger = Logger.getLogger("WikipulseResource");

	private final static String DB_PATH = "authorgraph-db/";

	private static enum RelTypes implements RelationshipType {
		EDITED
	}

	private static GraphDatabaseService graphDb;

	// ---
	
	public static List<Page> findInterestingPages(List<Page> pages) {
		initDatabase();

		insertPagesAndAuthorsIntoDatabase(pages);
		
		rankPages(pages);

		// TODO Auto-generated method stub
		return pages;
	}
	
	// ---

	private static void insertPagesAndAuthorsIntoDatabase(List<Page> pages) {
		logger.info("Begin updating nodes");
		Transaction tx = graphDb.beginTx();
		try {
			for (Page p : pages) {
				Node pagenode = getOrCreateNodeWithUniqueFactory(p.getPageId(), graphDb, "pages");
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
						Node authornode = getOrCreateNodeWithUniqueFactory(we.getUser(), graphDb, "authors");
						
						// check if author->page relationship already exists
						Iterable<Relationship> relationships = authornode.getRelationships(RelTypes.EDITED);
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
							match = authornode.createRelationshipTo( pagenode, RelTypes.EDITED );
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
		logger.info("End updating nodes");
	}
	
	private static void rankPages(List<Page> pages) { // TODO !!!
		//ExecutionEngine engine = new ExecutionEngine( graphDb );
		
		//for (Page p: pages) {
			//ExecutionResult authors = engine.execute( "start n=node("+p.getPageId()+") return n, n.id" );
		
		//}
		
		

		/**
		for (Node n: graphDb.getAllNodes()) {
			System.out.println(n.getId()+": "+n.getProperty("id")+", "+n.getProperty("timestamp"));
		}
		**/
		
		/**
		IndexManager index = graphDb.index();
		Index<Node> pagesIndex = index.forNodes( "pages" );
		Index<Node> authorsIndex = index.forNodes( "authors" );
		
		for (Page p: pages) {
			// Get all authors for p
			List<String> authorList = new LinkedList<String>();
			IndexHits<Node> hits = pagesIndex.get("id", p.getPageId());
			Node pageNode = hits.getSingle();
			
			Iterable<Relationship> edited = pageNode.getRelationships(RelTypes.EDITED);
			
			for (Relationship r: edited) {
				Node authorNode = r.getOtherNode(pageNode);
				
				authorList.add(authorNode.getProperty("id").toString()); // TODO
			}
			
			
			
		}
		**/
		
		/**
		for (NewsItem i: newsItems) {
			List<WikiEdit> edits_i = i.getPage().getEdits();
			for (NewsItem j: newsItems) {
				if (! i.equals(j) ) {
					List<WikiEdit> edits_j = j.getPage().getEdits();
					for (WikiEdit e_i: edits_i) {
						for (WikiEdit e_j: edits_j) {
							if (e_i.getUser().equals(e_j.getUser())) {
								i.increaseRank();
							}
						}
					}
				}
			}
		}
	**/
	}

	// ---

	private static Node getOrCreateNodeWithUniqueFactory(String id, GraphDatabaseService graphDb, String index) {
		UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graphDb, index) {
			@Override
			protected void initialize(Node created, Map<String, Object> properties) {
				created.setProperty("id", properties.get("id"));
				logger.info("Creating new node with id '"+properties.get("id")+"'");
			}
		};

		return factory.getOrCreate("id", id);
	}

	// ---

	private static void initDatabase() {
		if (graphDb == null) {
			graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
			registerShutdownHook(graphDb);
		}
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running example before it's completed)
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}
}
