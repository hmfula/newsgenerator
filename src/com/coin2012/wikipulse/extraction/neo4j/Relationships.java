package com.coin2012.wikipulse.extraction.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum Relationships implements RelationshipType {
	HAS, BASED_ON, BASED_ON_EDIT_OF, EDITED

}
