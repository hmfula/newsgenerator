package com.coin2012.wikipulse.extraction.statsgrok;



import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.restlet.resource.ClientResource;

public abstract class StatsGrokQueries {
	// Last 30
	private final static String statsGrokApiLast30days = "http://stats.grok.se/json/en/latest30/";
	
	public static ClientResource buildQuery(String pageTitle){
		URI uri = null;
		URL url;
		try {
			url = new URL(statsGrokApiLast30days+pageTitle);
			uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new ClientResource(uri);
	}
}
