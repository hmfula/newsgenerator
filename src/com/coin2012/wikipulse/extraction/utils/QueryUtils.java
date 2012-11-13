package com.coin2012.wikipulse.extraction.utils;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public abstract class QueryUtils {

	public static String executeQueryToResource(ClientResource resource) {
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
