package com.coin2012.wikipulse.api;

import java.util.List;

/**
 * Defines the interface for the logic that is offered through the REST routes
 * in the WikipulseResource class.
 * 
 * 
 */
public interface WikipulseService {

	/**
	 * Return a list of news as string in JSON format.
	 * 
	 * @param nprop
	 *            - specifies additional content for the returned news sperated
	 *            by |. img = news includes images
	 *            relevance rating.
	 * @return list of news as JSON string.
	 */
	public String getNews(String nprop);

	/**
	 * Returns a list of news for a given category as string in JSON format.
	 * 
	 * @param category
	 *            - category of the returned news.
	 * @param nprop
	 *            - specifies additional content for the returned news sperated
	 *            by |. img = news includes images
	 * @return list of news as JSON string.
	 */
	public String getNewsForCategory(String category, String nprop);

	/**
	 * Returns a list of pages with changes in the last 2h as string in JSON
	 * format.
	 * 
	 * @param minChanges
	 *            - amount of changes necessary for a page to added to the
	 *            return string. Default value is 10.
	 * @return list of pages with recent changes as JSON string-
	 */
	public String getRecentChanges(String minChanges);
	
	/**
	 * Returns a top10 list of news that has been read by users
	 * 
	 * @return list of top10 news  as JSON string-
	 */
	public String getMostReadNews();
	
	public void saveUserInteraction(String News);

	/**
	 * This method returns edit information of Wikipedians.
	 * @param editorNames a list of Wikipedians user names which you would like to query
	 * @return JSON string of editor information
	 */
	public String getEditors(List<String> editorNames);

	//
	// /**
	// * Fetches pages for a given category.
	// *
	// * @param category
	// * of articles
	// * @return pages for a given category
	// */
	// public String getMostReadTitlesForCategory(String category);
	//
	// /**
	// * Searches for pages matching given search text
	// *
	// * @param searchText used during search operation in Wikipedia API
	// * @return page snippets (SnippetPage extends Page class) which contain
	// matching <code>searchText<code>
	// */
	// public String searchForPagesThatMatch(String searchText);
	//
	// /**
	// * Lists external pages referencing a given page
	// *
	// * @param url the source url
	// * @return pages which externally link to url
	// *
	// */
	// public String searchForPagesReferencing(String url);
	//
	// /**
	// * Returns a list of Pages that contain images found in a given page
	// * @param pageTitle used to search the page for images
	// * @return page (s) with images
	// */
	// public String getPageWithImages(String pageTitle);

	// public String getMostReadTitlesForCategory(String category);
	//
	// /**
	// * Searches for pages matching given search text
	// *
	// * @param searchText used during search operation in Wikipedia API
	// * @return page snippets (SnippetPage extends Page class) which contain
	// matching <code>searchText<code>
	// */
	// public String searchForPagesThatMatch(String searchText);
	//
	// /**
	// * Lists external pages referencing a given page
	// *
	// * @param url the source url
	// * @return pages which externally link to url
	// *
	// */
	// public String searchForPagesReferencing(String url);
	//
	// /**
	// * Returns a list of Pages that contain images found in a given page
	// * @param pageTitle used to search the page for images
	// * @return page (s) with images
	// */
	// public String getPageWithImages(String pageTitle);

	//
	// /**
	// * Fetches pages for a given category.
	// *
	// * @param category
	// * of articles
	// * @return pages for a given category
	// */
	// public String getMostReadTitlesForCategory(String category);
	//
	// /**
	// * Searches for pages matching given search text
	// *
	// * @param searchText used during search operation in Wikipedia API
	// * @return page snippets (SnippetPage extends Page class) which contain
	// matching <code>searchText<code>
	// */
	// public String searchForPagesThatMatch(String searchText);
	//
	// /**
	// * Lists external pages referencing a given page
	// *
	// * @param url the source url
	// * @return pages which externally link to url
	// *
	// */
	// public String searchForPagesReferencing(String url);
	//
	// /**
	// * Returns a list of Pages that contain images found in a given page
	// * @param pageTitle used to search the page for images
	// * @return page (s) with images
	// */
	// public String getPageWithImages(String pageTitle);

}