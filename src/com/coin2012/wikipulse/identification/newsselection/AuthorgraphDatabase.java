package com.coin2012.wikipulse.identification.newsselection;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.UniqueFactory;

import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class AuthorgraphDatabase {

	private final static String DB_PATH = "authorgraph-db/";
	
	private static Logger logger = Logger.getLogger("AuthorgraphDatabase.class");
	
	private static GraphDatabaseService graphDb;
	
	/**
	 * 
	 * @author Stefan
	 *
	 *	The types of edges between the nodes in the database:
	 *	- EDITED: author edited page
	 *  - BASED_ON: news is based on page
	 *  - BASED_ON_EDIT_OF: news is based on edit of author
	 */
	public static enum RelTypes implements RelationshipType {
		EDITED, BASED_ON, BASED_ON_EDIT_OF
	}

	public static GraphDatabaseService getGraphDatabaseServiceInstance() {
		if (graphDb == null) {
			graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
			registerShutdownHook(graphDb);
		}
		
		return graphDb;
	}
	
	public static List<News> getLatestNews() {
		// TODO
	}
	
	public static News getNews(String newsid) {
		Index<Node> nodeIndex = getGraphDatabaseServiceInstance().index().forNodes( "pages" );

		Node node = nodeIndex.get("id", newsid).getSingle();
		News news = new News();
		
		news.setNews(node.getProperty("").toString()); // TODO
		news.setPagetTitle(node.getProperty("").toString()); // TODO
		news.setViewCount((Integer) node.getProperty("")); // TODO
		
		if (node.getRelationships(RelTypes.BASED_ON).iterator().hasNext()) {
			Relationship pageRelationship = node.getRelationships(RelTypes.BASED_ON).iterator().next();
			Node page = pageRelationship.getOtherNode(node);
			news.setPageId(page.getProperty("id").toString());
		}
		
		return news;
	}
	
	public static Node getPage(String pageid) {
		Index<Node> nodeIndex = getGraphDatabaseServiceInstance().index().forNodes( "pages" );

		return nodeIndex.get("id", pageid).getSingle();
	}
	
	public static Node saveAuthor(String name) {
		return getOrCreateNodeWithUniqueFactory(name, "authors");
	}
	
	public static Node savePage(Page p) {
		
		Transaction tx = getGraphDatabaseServiceInstance().beginTx();
		try {
			Node pagenode = getOrCreateNodeWithUniqueFactory(p.getPageId(), "pages");
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
						Node authornode = saveAuthor(we.getUser());
						
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
							match.setProperty("editcount", 1);
						}
						
						// update edit count
						int editCount = ((Integer) match.getProperty("editcount")).intValue();
						editCount++;
						match.setProperty("editcount", editCount);
					}
		}

			tx.success();
			return pagenode;
		} catch (Exception e) {
			e.printStackTrace();
			tx.failure();
			return null;
		} finally {
			tx.finish();
		}
	}
	
	public static Node saveNews(News n) {
		// TODO create id
		Node newsnode = getOrCreateNodeWithUniqueFactory(n.getId(), "news");
		
		Node page = getPage(n.getPageId());
		newsnode.createRelationshipTo(page, RelTypes.BASED_ON);
		
		
		// TODO save properties and create edges to page/authors
		
		return newsnode;
	}
	
	// ---
	
	private static Node getOrCreateNodeWithUniqueFactory(String id, String index) {
		UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(getGraphDatabaseServiceInstance(), index) {
			@Override
			protected void initialize(Node created, Map<String, Object> properties) {
				created.setProperty("id", properties.get("id"));
				logger.info("Creating new node with id '"+properties.get("id")+"'");
			}
		};

		return factory.getOrCreate("id", id);
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
