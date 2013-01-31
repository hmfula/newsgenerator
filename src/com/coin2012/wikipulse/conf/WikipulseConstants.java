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
	public static final float YESTERDAY_RELEVANCE_DEFAULT_VALUE = 0.1f;
	
	public static final int IDENTIFICATION_RUNNER_SLEEP = 60000;
	public static final boolean WRITE_RANK_DATA_FILE = true;
	
	public static final double MIN_PAGERANK = 0.25d;
	public static final double AUTHORS_WITH_NEWS_MODIFIER = 3d;
	public static final double DOMAIN_EXPERTS_MODIFIER = 2d;
	public static final double COMMON_AUTHROS_MODIFIER = 2d;
	public static final double RELEVANCE_MODIFIER = 1d;
	public static final double RECENT_CHANGES_MODIFIER = 0.05d;
}
