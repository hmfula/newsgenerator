package com.coin2012.wikipulse.extraction.neo4j;

import java.util.Map;
import java.util.UUID;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.UniqueFactory;

import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;
import com.google.gson.Gson;

/**
 * Handles the saving of objects into the WikipulseGraphDatabase.
 * @author Karsten
 *
 */
public class ObjectSaver {

	private GraphDatabaseService graphDB;

	/**
	 * Saves if the page is new or updates if the page already exists in the DB.
	 * Generates EDITED relationships with contributing authors and HAS
	 * relationships with all Categories of the page.
	 * 
	 * @param page
	 */
	public void saveOrUpdatePage(Page page) {
		graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		Transaction tx = graphDB.beginTx();
		try {
			Node pageNode = this.getOrCreateNodeWithUniqueFactory(page.getPageId(), "pages");
			pageNode.setProperty("title", page.getTitle());
			pageNode.setProperty("type", "page");
			for (WikiEdit edit : page.getEdits()) {
				Node editorNode = this.getOrCreateNodeWithUniqueFactory(edit.getUserId(), "authors");
				editorNode.setProperty("user", edit.getUser());
				this.createUniqueRelationshipWithProperty(editorNode, Relationships.EDITED, pageNode, "revid", edit.getRevid());
			}
			for (Category category : page.getCategories()) {
				Node categoryNode = this.getOrCreateNodeWithUniqueFactory("title", category.getTitle(), "categories");
				categoryNode.setProperty("type", "category");
				this.createUniqueRelationship(pageNode, Relationships.HAS, categoryNode);
			}
			tx.success();
		} finally {
			tx.finish();
		}
	};

	/**
	 * Saves a News object if it has no id. Therefore generates an UUID and
	 * saves the news object as a node. Furthermore a Relationship with the page
	 * the news is BASED_ON and a BASED_ON_EDIT_OF Relationship with the
	 * contributing Author are created.
	 * 
	 * @param news
	 *            - object to be saved.
	 */
	public String saveNews(News news) {
		String id = null;
		if (news.getId() == null || news.getId().equals("")) {
			// 2^122 Possibilities => nearly no chance for double
			id = UUID.randomUUID().toString();
			long viewCount = 0;
			graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
			Transaction tx = graphDB.beginTx();
			try {
				Node newsNode = this.getOrCreateNodeWithUniqueFactory(id, "news");
				newsNode.setProperty("news", news.getNews());
				newsNode.setProperty("shortNews", news.getShortNews());
				newsNode.setProperty("timestamp", TimestampGenerator.generateTimestamp());
				newsNode.setProperty("viewCount", viewCount);
				Gson gson = new Gson();
				newsNode.setProperty("imageUrlList", gson.toJson(news.getImageUrlList()));
				Node pageNode = this.getOrCreateNodeWithUniqueFactory(news.getPageId(), "pages");
				this.createUniqueRelationship(newsNode, Relationships.BASED_ON, pageNode);
				for (Editor editor : news.getEditors()) {
					Node editorNode = this.getOrCreateNodeWithUniqueFactory(editor.getUserid(), "authors");
					this.createUniqueRelationship(newsNode, Relationships.BASED_ON_EDIT_OF, editorNode);
				}
				tx.success();
			} finally {
				tx.finish();
			}
		}
		return id;
	};

	/**
	 * Increases the view count for a news by one.
	 * 
	 * @param newsId
	 *            - id of the news.
	 */
	public void updateViewCount(String newsId) {
		graphDB = WikipulseGraphDatabase.getGraphDatabaseServiceInstance();
		Transaction tx = graphDB.beginTx();
		try {
			Node newsNode = this.getOrCreateNodeWithUniqueFactory(newsId, "news");
			long viewCount = (Long)newsNode.getProperty("viewCount");
			viewCount = viewCount + 1;
			newsNode.setProperty("viewCount", viewCount);
			tx.success();
		} finally {
			tx.finish();
		}
	}

	private Node getOrCreateNodeWithUniqueFactory(final String key, String value, String index) {
		UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graphDB, index) {
			@Override
			protected void initialize(Node created, Map<String, Object> properties) {
				created.setProperty(key, properties.get(key));
			}
		};
		return factory.getOrCreate(key, value);
	}

	private Node getOrCreateNodeWithUniqueFactory(String id, String index) {
		return this.getOrCreateNodeWithUniqueFactory("id", id, index);
	}

	private void createUniqueRelationship(Node startNode, Relationships relationshipType, Node endNode) {
		for (Relationship relationship : startNode.getRelationships(relationshipType, Direction.OUTGOING)) {
			if (relationship.getEndNode().equals(endNode)) {
				return;
			}
		}
		startNode.createRelationshipTo(endNode, relationshipType);
	}

	private void createUniqueRelationshipWithProperty(Node startNode, Relationships relationshipType, Node endNode, String property, String value) {
		for (Relationship relation : startNode.getRelationships(relationshipType, Direction.OUTGOING)) {
			if (relation.getEndNode().equals(endNode) && relation.getProperty(property) == value) {
				return;
			}
		}
		Relationship relationship = startNode.createRelationshipTo(endNode, relationshipType);
		relationship.setProperty(property, value);
	}
}
