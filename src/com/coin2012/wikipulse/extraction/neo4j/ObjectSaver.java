package com.coin2012.wikipulse.extraction.neo4j;

import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.UniqueFactory;

import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class ObjectSaver {
	
	private GraphDatabaseService graphDB;
	
	public void saveOrUpdatePage(Page page){
		graphDB = AuthorgraphDatabase.getGraphDatabaseServiceInstance();
		Transaction tx = graphDB.beginTx();
		try
		{
		    Node pageNode = this.getOrCreateNodeWithUniqueFactory(page.getPageId(), "pages");
		    pageNode.setProperty("title", page.getTitle());
		    for (WikiEdit edit : page.getEdits()) {
				Node editorNode = this.getOrCreateNodeWithUniqueFactory(edit.getUserId(), "authors");
				editorNode.setProperty("username", edit.getUser());
				this.createUniqueRelationshipWithProperty(editorNode, Relationships.EDITED, pageNode, "revid", edit.getRevid());
			}
		    for (Category category : page.getCategories()) {
				Node categoryNode = this.getOrCreateNodeWithUniqueFactory(category.getTitle(), "categories");
				this.createUniqueRelationship(pageNode, Relationships.HAS, categoryNode);
			}
		    tx.success();
		}
		finally
		{
		    tx.finish();
		}
	};
	
	public void saveAuthor(Editor editor){
		graphDB = AuthorgraphDatabase.getGraphDatabaseServiceInstance();
		Transaction tx = graphDB.beginTx();
		try
		{
		    Node editorNode = this.getOrCreateNodeWithUniqueFactory(editor.getUserid(), "authors");
		    editorNode.setProperty("name", editor.getName());
		    editorNode.setProperty("editcount", editor.getEditcount());
		    tx.success();
		}
		finally
		{
		    tx.finish();
		}
	};
	

	public void saveNews (News news){
		graphDB = AuthorgraphDatabase.getGraphDatabaseServiceInstance();
		Transaction tx = graphDB.beginTx();
		try
		{
			//TODO news Id generation
		    Node newsNode = this.getOrCreateNodeWithUniqueFactory("1", "authors");
		    newsNode.setProperty("news", news.getNews());
		    Node pageNode = this.getOrCreateNodeWithUniqueFactory(news.getPageId(), "pages");
		    this.createUniqueRelationship(newsNode, Relationships.BASED_ON, pageNode);
		    Node editorNode = this.getOrCreateNodeWithUniqueFactory(news.getEditor().getUserid(), "authors");
		    this.createUniqueRelationship(newsNode, Relationships.BASED_ON_EDIT_OF, editorNode);
		    tx.success();
		}
		finally
		{
		    tx.finish();
		}
	};
	
	private Node getOrCreateNodeWithUniqueFactory(String id, String index) {
		UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graphDB, index) {
			@Override
			protected void initialize(Node created, Map<String, Object> properties) {
				created.setProperty("id", properties.get("id"));
			}
		};
		return factory.getOrCreate("id", id);
	}
	
	private void createUniqueRelationship(Node startNode, Relationships relationshipType, Node endNode){
		for (Relationship relationship : startNode.getRelationships(relationshipType, Direction.OUTGOING)){
			if(relationship.getEndNode().equals(endNode)){
				return;
			}
		}
		startNode.createRelationshipTo(endNode, relationshipType);
	}

	private void createUniqueRelationshipWithProperty(Node startNode, Relationships relationshipType, Node endNode, String property, String value){
		for (Relationship relation : startNode.getRelationships(relationshipType, Direction.OUTGOING)){
			if(relation.getEndNode().equals(endNode) && relation.getProperty(property) == value){
				return;
			}
		}
		Relationship relationship = startNode.createRelationshipTo(endNode, relationshipType);
		relationship.setProperty(property, value);
	}
}
