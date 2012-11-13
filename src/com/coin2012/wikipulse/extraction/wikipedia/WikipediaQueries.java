package com.coin2012.wikipulse.extraction.wikipedia;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.restlet.resource.ClientResource;

public abstract class WikipediaQueries {
	private final static String wikipediaApi = "http://en.wikipedia.org/w/api.php";
	
	public static ClientResource buildQueryForPagesInCategory(String category) {
		ClientResource resource = new ClientResource(wikipediaApi);
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("list", "categorymembers");
		resource.getReference().addQueryParameter("cmtitle", category);
		resource.getReference().addQueryParameter("cmsort", "timestamp");
		resource.getReference().addQueryParameter("cmdir", "desc");
		resource.getReference().addQueryParameter("cmprop", "ids|title|timestamp");
		resource.getReference().addQueryParameter("cmlimit", "max");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}
	
	//TODO 2012-11-13T15:03:25Z
	/**
	 * http://en.wikipedia.org/w/api.php?action=query&pageids=DUMMY&prop=revisions&rvprop=ids|timestamp|comment|user&rvend=DATE&rvdir=older&format=json
	 * @param pageId
	 * @return
	 */
	public static ClientResource buildQueryForRevisions(String pageId) {
		ClientResource resource = new ClientResource(wikipediaApi);
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("pageids", pageId);
		resource.getReference().addQueryParameter("prop", "revisions");
		//resource.getReference().addQueryParameter("rvprop", "content|ids|timestamp|flags|comment|user");
		resource.getReference().addQueryParameter("rvprop", "ids|timestamp|flags|comment|user");
//		resource.getReference().addQueryParameter("rvlimit", "1");
		resource.getReference().addQueryParameter("rvend", generateTimestampForToday());
		resource.getReference().addQueryParameter("rvdir", "older");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}
	
	private static String generateTimestampForToday(){
		String DATE_PATTERN = "yyyyMMdd000000";
		SimpleDateFormat dateFormat=new SimpleDateFormat(DATE_PATTERN);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println(dateFormat.format(new Date()));
		return dateFormat.format(new Date());
	}

}
