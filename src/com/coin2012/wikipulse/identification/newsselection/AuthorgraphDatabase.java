package com.coin2012.wikipulse.identification.newsselection;

import java.util.Map;
import java.util.logging.Logger;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.UniqueFactory;

public class AuthorgraphDatabase {

	private final static String DB_PATH = "authorgraph-db/";
	
	private static Logger logger = Logger.getLogger("AuthorgraphDatabase.class");
	
	private static GraphDatabaseService graphDb;
	

	public static enum RelTypes implements RelationshipType {
		EDITED
	}
	
	private AuthorgraphDatabase() {	}

	public static GraphDatabaseService getGraphDatabaseServiceInstance() {
		if (graphDb == null) {
			graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
			registerShutdownHook(graphDb);
		}
		
		return graphDb;
	}

	public static Node getOrCreateNodeWithUniqueFactory(String id, String index) {
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
