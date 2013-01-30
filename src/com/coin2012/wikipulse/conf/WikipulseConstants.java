package com.coin2012.wikipulse.conf;

/**
 * 
 * This class store constants used throughout the application.
 *
 */
public abstract class WikipulseConstants {
	public final static String WIKIPEDIA_API_QUERY_BASE_URL = "http://en.wikipedia.org/w/api.php";
	public final static String WIKIPEDIA_PAGE_BASE_URL = "en.wikipedia.org/wiki/";
	public final static  int MAXIMUM_ALLOWED_NUMBER_OF_SENTENCES = 20;
	public static final String SEPARATOR = "/";
	public static final String SPACE = " ";
	public static final String EMPTY = "";
	
	
	/**
	 * WORKING CONSTANTS SHOULD BE SET ACCORDING TO TRAFFIC CHARACTERISTICS
	 * THE CURRENT VALUES ARE SET TO 1 JUST TO TEST THE APPLICATION
	 * FEEL FREE TO ADJUST THEM ACCORDINGLY
	 */
	public static final int THRESHOLD_NUMBER_OF_EDITS_FOR_DOMAIN_EXPERT = 1;
	public static final int THRESHOLD_FOR_NEWS_CONTRIBUTOR = 1;
	public static final int THRESHOLD_FOR_TOP_CONTRIBUTOR = 1;

	/**
	 * Constants for page indentification
	 */
	public static final boolean WRITE_RANK_DATA_FILE = true;
	public static final int MIN_PAGERANK = 100;
	public static final int AUTHORS_WITH_NEWS_MODIFIER = 50;
	public static final int DOMAIN_EXPERTS_MODIFIER = 50;
	public static final int COMMON_AUTHROS_MODIFIER = 1;
	public static final int RELEVANCE_MODIFIER = 100;
}
