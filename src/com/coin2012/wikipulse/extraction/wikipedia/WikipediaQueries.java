package com.coin2012.wikipulse.extraction.wikipedia;

import org.restlet.resource.ClientResource;

import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;

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
		resource.getReference().addQueryParameter("rvend", TimestampGenerator.generateTimestampForToday());
		resource.getReference().addQueryParameter("rvdir", "older");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}
	
//	public static ClientResource buildQuerySearchForPagesThatMatch(
//			String searchText) {
//		ClientResource resource = new ClientResource(wikipediaApi);
//		resource.getReference().addQueryParameter("action", "query");
//		resource.getReference().addQueryParameter("list", "search");
//		resource.getReference().addQueryParameter("srwhat", "text");
//		resource.getReference().addQueryParameter("srsearch", searchText);
//		resource.getReference().addQueryParameter("format", "json");
//		return resource;
//	}
	
	// /**
	// * Builds a query that lists pages referencing a particular url. Useful
	// for finding external links
	// * @see
	// http://en.wikipedia.org/w/api.php?action=query&list=exturlusage&euquery=httpd.apache.org&eulimit=30&eunamespace=0&format=json
	// * @param url
	// * @return resource
	// */
	// public static ClientResource buildQueryToSearchForPagesReferencing(
	// String url) {
	// ClientResource resource = new ClientResource(wikipediaApi);
	// resource.getReference().addQueryParameter("action", "query");
	// resource.getReference().addQueryParameter("list", "exturlusage");
	// resource.getReference().addQueryParameter("euquery", url);
	// resource.getReference().addQueryParameter("eulimit", "30");
	// resource.getReference().addQueryParameter("eunamespace", "0");
	// resource.getReference().addQueryParameter("format", "json");
	// return resource;
	// }

	/**
	 * Builds a query that used to fetch top 5 images about a given a title
	 * @param pageTitle being used to fetch the images
	 * @return a resource
	 */
	public static ClientResource buildQueryForImagesFileNames(String pageTitle) {
		ClientResource resource = new ClientResource(wikipediaApi);
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("titles", pageTitle);
		resource.getReference().addQueryParameter("prop", "images");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}

	public static ClientResource buildQueryForImagesUrlsOfImageFileNames(String imageFileNames) {
		ClientResource resource = new ClientResource(wikipediaApi);
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("titles", imageFileNames);
		resource.getReference().addQueryParameter("prop", "imageinfo");
		resource.getReference().addQueryParameter("iiprop", "url");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}
	
	/**
	 * http://en.wikipedia.org/w/api.php?action=query&list=recentchanges&rcprop=title|ids|sizes|flags|user|timestamp&rcend=2012113140000&rclimit=500&format=json
	 * @return
	 */
	public static ClientResource buildQueryForRecentChanges(String rcstart,String rcend) {
		ClientResource resource = new ClientResource(wikipediaApi);
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("list", "recentchanges");
		resource.getReference().addQueryParameter("rcprop", "title|ids|sizes|flags|user|timestamp");
		resource.getReference().addQueryParameter("rclimit", "500");
		resource.getReference().addQueryParameter("rcend", rcend);
		resource.getReference().addQueryParameter("rcstart", rcstart);
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}
}
