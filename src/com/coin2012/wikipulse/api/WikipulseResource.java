package com.coin2012.wikipulse.api;

import static spark.Spark.get;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.extraction.wikipedia.polling.RecentChangesRunnable;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

/**
 * This class initializes and starts up the WikipulseResource through the main
 * method to run stand alone on an embedded Jetty server or through the init
 * method when run on a Tomcat server. It defines which rest routes are offered
 * to the frontend and also starts db setups and necessary polling threads.
 * 
 * 
 */
public class WikipulseResource implements SparkApplication {

	private static Logger logger = Logger.getLogger("WikipulseResource");

	/**
	 * Starts the WikipulseResource with an embedded Jetty server on
	 * http://localhost:4567
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		createRESTRoutes();
		createInMemDb();
	}

	/**
	 * Starts the WikipulseResource when run on a tomcat server.
	 */
	public void init() {
		createRESTRoutes();
		createInMemDb();
	}

	/**
	 * Defines all alvailable REST routes. There are three routes offered. The
	 * first route /news offers news without focusing on any topic. This route
	 * can be parameterized by the query parameter nprop. nprop can have
	 * multiple values separated by |. The currently possible values are img.
	 * img includes images into the news. The second route is /news/:category
	 * which offers news for a given category. This route also allows the query
	 * parameter nprop. The third offered route is /changes which offer a list
	 * of wikipedia pages with recent changes. The list can be reduced by adding
	 * the query parameter minChanges. If minChanges is set only pages with
	 * atleast that amount of changes are returned.
	 */
	private static void createRESTRoutes() {
		get(new Route("/news") {
			@Override
			public Object handle(Request request, Response response) {
				WikipulseService wikipulseService = new WikipulseServiceImpl();
				String nprop = request.queryParams("nprop");
				response.type("application/json; charset=utf-8");
				return wikipulseService.getNews(nprop);
			}
		});

		get(new Route("/news/:category") {
			@Override
			public Object handle(Request request, Response response) {
				WikipulseService wikipulseService = new WikipulseServiceImpl();
				String category = request.params(":category");
				String nprop = request.queryParams("nprop");
				response.type("application/json; charset=utf-8");
				return wikipulseService.getNewsForCategory(category, nprop);
			}
		});

		get(new Route("/changes") {
			@Override
			public Object handle(Request request, Response response) {
				WikipulseService wikipulseService = new WikipulseServiceImpl();
				response.type("application/json; charset=utf-8");
				String minChanges = request.queryParams("minchanges");
				return wikipulseService.getRecentChanges(minChanges);
			}
		});
		
		get(new Route("/top_user_news") {
			@Override
			public Object handle(Request request, Response response) {
				WikipulseService wikipulseService = new WikipulseServiceImpl();
				response.type("application/json; charset=utf-8");
				return wikipulseService.getMostReadNews();
			}
		});
		
		get(new Route("/user_interaction") {
			@Override
			public Object handle(Request request, Response response) {
				WikipulseService wikipulseService = new WikipulseServiceImpl();
				response.type("application/json; charset=utf-8");
				String news = request.queryParams("news");
				//if(news.matches("^[a-zA-Z_]+$")){
					wikipulseService.saveUserInteraction(news);
				//}
				String redirect_url = request.queryParams("redirect_url");
				if (redirect_url != ""){
					response.redirect(redirect_url);	
				}				
				return "success";
			}
		});
		
		/**
		 * This route is used only for testing.
		 * Currently hard coded to show only top 10 Wikipedians
		 * To be deleted after integration  with identification algorithm.
		 * Note:The list is randomized to test sort functionality. Coded to return results in descending order(i.e return biggest edit count first).
		 */
		get(new Route("/listeditors") {
			@Override
			public Object handle(Request request, Response response) {
				WikipulseService wikipulseService = new WikipulseServiceImpl();
				response.type("application/json; charset=utf-8");
//				String editorNames = request.queryParams("ususers");
				List <String> editorNames = new ArrayList<String>();
				editorNames.add("Woohookitty");
				editorNames.add("Bearcat");
				editorNames.add("Tassedethe");
				editorNames.add("Rjwilmsi");
				editorNames.add("Rich Farmbrough");
				editorNames.add("Waacstats");
				editorNames.add("Hmains");
				editorNames.add("Ser Amantio di Nicolao");
				editorNames.add("Dr. Blofeld");
				editorNames.add("Koavf");
				
				return wikipulseService.getEditors(editorNames);
			}
		});

		// get(new Route("/MostReadArticlesInCategory") {
		// @Override
		// public Object handle(Request request, Response response) {
		// String category = "Category:" + request.queryParams("category");
		// WikipulseService wikipulseService = new WikipulseServiceImpl();
		// response.type("application/json; charset=utf-8");
		// String return_set = "callback(" +
		// wikipulseService.getMostReadTitlesForCategory(category) + ")";
		// return return_set;
		// }
		// });
		//
		// //Example
		// http://localhost:4567/ExernalLinkSearch?url=www.mittromney.com
		// //Example can be used for result verification.
		// http://en.wikipedia.org/w/api.php?action=query&list=exturlusage&euquery=www.mittromney.com&eulimit=30&eunamespace=0&format=json
		// get(new Route("/ExernalLinkSearch") {
		// @Override
		// public Object handle(Request request, Response response) {
		// String url = request.queryParams("url");
		// WikipulseService wikipulseService = new WikipulseServiceImpl();
		// response.type("application/json; charset=utf-8");
		// String externallyLinkePages =
		// wikipulseService.searchForPagesReferencing(url);
		//
		// return externallyLinkePages;
		// }
		// });
		//
		//
		// //Example: to search for pages containing text '2012 US Elections'.
		// http://localhost:4567/FreeTextSearch?&srsearch=2012 US Elections
		// get(new Route("/FreeTextSearch") {
		// @Override
		// public Object handle(Request request, Response response) {
		// String searchText = request.queryParams("srsearch");
		// WikipulseService wikipulseService = new WikipulseServiceImpl();
		// response.type("application/json; charset=utf-8");
		// String snippetPages = "callback(" +
		// wikipulseService.searchForPagesThatMatch(searchText) + ")";
		// return snippetPages;
		// }
		//
		// });
		//
		//
		// // Example: To list images on Albert Einstein page.
		// http://localhost:4567/FetchPageImages?titles=Albert%20Einstein
		//
		// get(new Route("/FetchPageImages") {
		//
		// @Override
		// public Object handle(Request request, Response response) {
		//
		// String imageTitle = request.queryParams("titles");
		// WikipulseService wikipulseService = new WikipulseServiceImpl();
		// response.type("application/json; charset=utf-8");
		// String pageDetails = "pic_callback(" +
		// wikipulseService.getPageWithImages(imageTitle) + ")";
		// return pageDetails;
		// }
		//
		// });

	}

	/**
	 * Sets up the in memory db and starts the polling needed to offer pages
	 * with recent changes through the /changes route.
	 */
	private static void createInMemDb() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			connection.createStatement()
					.execute("CREATE TABLE changes (timestamp varchar(20), pageTitle varchar(255),UNIQUE (timestamp, pageTitle))");
			connection.createStatement()
					.execute("CREATE TABLE mostreadnews (article varchar(255), numberofclicks integer,UNIQUE (article))");
			new Thread(new RecentChangesRunnable()).start();
		} catch (SQLException e) {
			logger.severe("Creation of in memory db table changes failed.");
		} catch (ClassNotFoundException e) {
			logger.severe("Loading of necessary db classes failed.");
		}

	}
	
	
}
