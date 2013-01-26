package com.coin2012.wikipulse.extraction.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class WikipulseGraphDatabase {

	private final static String DB_PATH = "authorgraph-db/";
	
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


	public static GraphDatabaseService getGraphDatabaseServiceInstance() {
		if (graphDb == null) {
			graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
			registerShutdownHook(graphDb);
		}
		
		return graphDb;
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
