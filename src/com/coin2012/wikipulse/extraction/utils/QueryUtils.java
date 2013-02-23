package com.coin2012.wikipulse.extraction.utils;

import java.io.IOException;
import java.util.logging.Level;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

/**
 * Contains utilities for queries.
 */
public abstract class QueryUtils {
	

	/**
	 * Executes a get request on the specified ClientResource and returns the result as a String
	 * @param resource - where to execute the query
	 * @return result as String
	 */
	public static String executeQueryToResource(ClientResource resource) {

		resource.getLogger().setLevel(Level.WARNING);
		Representation response = resource.get();

		String result = "error";
		try {
			result = response.getText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
