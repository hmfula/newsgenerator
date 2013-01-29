package com.coin2012.wikipulse.extraction.wikipedia;

import org.restlet.resource.ClientResource;

import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;

public abstract class WikipediaQueries {
	private final static String wikipediaApi = "http://en.wikipedia.org/w/api.php";
	
	public static ClientResource buildQueryForPagesInCategory(String category) {
		ClientResource resource = new ClientResource(wikipediaApi);
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("list", "categorymembers");
		resource.getReference().addQueryParameter("cmtitle", "Category:" + category);
		resource.getReference().addQueryParameter("cmsort", "timestamp");
		resource.getReference().addQueryParameter("cmdir", "desc");
		resource.getReference().addQueryParameter("cmprop", "ids|title|timestamp");
		resource.getReference().addQueryParameter("cmlimit", "max");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}
	
	public static ClientResource buildQueryForPageWithCategoriesByPageId(String pageid) {
		ClientResource resource = new ClientResource(wikipediaApi);
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("pageids", pageid);
		resource.getReference().addQueryParameter("prop", "categories");;
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
	
	public static ClientResource buildQueryForRevisionsFromTheLastTwoHours(String pageId) {
		ClientResource resource = new ClientResource(wikipediaApi);
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("pageids", pageId);
		resource.getReference().addQueryParameter("prop", "revisions");
		resource.getReference().addQueryParameter("rvprop", "ids|timestamp|user|userid");
		resource.getReference().addQueryParameter("rvlimit", "500");
		resource.getReference().addQueryParameter("rvend", TimestampGenerator.generateTimestampFromTwoHoursAgo());
		resource.getReference().addQueryParameter("rvdir", "older");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}

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
		resource.getReference().addQueryParameter("rcnamespace", "0|1");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}

	/**
 * http://en.wikipedia.org/w/api.php?action=query&list=users&ususers=Koavf&usprop=editcount
 * @param editors
 * @return
 */
	public static ClientResource buildQueryToFetchWikipediaEditors(String editors) {
		ClientResource resource = new ClientResource(wikipediaApi);
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("list", "users");
		resource.getReference().addQueryParameter("ususers", editors);
		resource.getReference().addQueryParameter("usprop", "blockinfo|groups|editcount|registration");
//		resource.getReference().addQueryParameter("limit", "500");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}

	public static ClientResource buildQueryForRevisionContent(String revid) {
		ClientResource resource = new ClientResource(wikipediaApi);
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("revids", revid);
		resource.getReference().addQueryParameter("prop", "revisions");
		resource.getReference().addQueryParameter("rvprop", "content");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}
}
