package com.coin2012.wikipulse.api;

import static spark.Spark.get;
import static spark.Spark.put;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.extraction.wikipedia.polling.RecentChangesRunnable;
import com.coin2012.wikipulse.identification.Identifier;

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

	private static WikipulseService wikipulseService = new WikipulseServiceImpl();

	/**
	 * Starts the WikipulseResource with an embedded Jetty server on
	 * http://localhost:4567
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		createRESTRoutes();
		createInMemDb();
		startIdentificationThread();
	}

	/**
	 * Starts the WikipulseResource when run on a tomcat server.
	 */
	public void init() {
		createRESTRoutes();
		createInMemDb();
		startIdentificationThread();
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
				String sort = request.queryParams("sort");
				String limit = request.queryParams("limit");
				response.type("application/json; charset=utf-8");
				return wikipulseService.getNews(sort, limit);
			}
		});

		get(new Route("/news/:news") {
			@Override
			public Object handle(Request request, Response response) {
				String newsId = request.params(":news");
				response.type("application/json; charset=utf-8");
				return wikipulseService.getNews(newsId);
			}
		});

		get(new Route("/changes") {
			@Override
			public Object handle(Request request, Response response) {
				response.type("application/json; charset=utf-8");
				String minChanges = request.queryParams("minchanges");
				return wikipulseService.getRecentChanges(minChanges);
			}
		});

		get(new Route("/categories") {
			@Override
			public Object handle(Request request, Response response) {
				String limit = request.queryParams("limit");
				response.type("application/json; charset=utf-8");
				return wikipulseService.getCategories(limit);
			}
		});

		get(new Route("/categories/:category") {
			@Override
			public Object handle(Request request, Response response) {
				String category = request.params(":category");
				response.type("application/json; charset=utf-8");
				return wikipulseService.getNewsForCategory(category);
			}
		});

		put(new Route("/news/:news") {
			@Override
			public Object handle(Request request, Response response) {
				response.type("application/json; charset=utf-8");
				String news = request.params(":news");
				wikipulseService.saveUserInteraction(news);

				String redirect_url = request.queryParams("redirect_url");
				if (redirect_url != "") {
					response.redirect(redirect_url);
				}
				return "success";
			}
		});

		// TODO Clean up
		/**
		 * This route is used only for testing. Currently hard coded to show
		 * only top 10 Wikipedians To be deleted after integration with
		 * identification algorithm. Note:The list is randomized to test sort
		 * functionality. Coded to return results in descending order(i.e return
		 * biggest edit count first).
		 */
		get(new Route("/wikipedians_by_edit_counts") {
			@Override
			public Object handle(Request request, Response response) {
				response.type("application/json; charset=utf-8");
				// String editorNames = request.queryParams("ususers");
				List<String> editorNames = new ArrayList<String>();
				editorNames.add("Woohookitty");
				editorNames.add("Bearcat");
				editorNames.add("Dr. Blofeld");
				editorNames.add("Tassedethe");
				editorNames.add("Rjwilmsi");
				editorNames.add("Koavf");
				editorNames.add("Rich Farmbrough");
				editorNames.add("Waacstats");
				editorNames.add("Hmains");
				editorNames.add("Ser Amantio di Nicolao");

				return wikipulseService.getEditors(editorNames);
			}
		});

		// TODO Clean up
		// Example
		// http://localhost:4567/create_summary?url=http://en.wikipedia.org/wiki/Finland&length=7
		// Where:
		// create_summary => route name
		// http://en.wikipedia.org/wiki/Finland => article url
		// length=7 => number of sentences in from the article
		get(new Route("/create_summary") {
			@Override
			public Object handle(Request request, Response response) {
				response.type("application/json; charset=utf-8");

				String url = request.queryParams("url");
				String length = request.queryParams("length");

				return wikipulseService.summarizeArticle(url, length);
			}
		});
	}

	/**
	 * Sets up the in memory db and starts the polling needed to offer pages
	 * with recent changes through the /changes route.
	 */
	private static void createInMemDb() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			Connection connection = DriverManager.getConnection(
					"jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			connection
					.createStatement()
					.execute(
							"CREATE TABLE changes (timestamp varchar(20), pageTitle varchar(255), pageid varchar(255), UNIQUE (timestamp, pageTitle, pageid))");
			new Thread(new RecentChangesRunnable()).start();
			addShutdownHook(connection);
		} catch (SQLException e) {
			logger.severe("Creation of in memory db table changes failed.");
		} catch (ClassNotFoundException e) {
			logger.severe("Loading of necessary db classes failed.");
		}

	}

	/**
	 * Shuts down the in-memory database correctly in case of any shutdown
	 * command.
	 * 
	 * @param connection
	 *            the connection to the in-memory database
	 */
	private static void addShutdownHook(final Connection connection) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {

					if (connection != null) {
						connection.close();
					}

				} catch (Exception e) {
					logger.info("Failed to close the connection to the database because of : "
							+ e.getCause());
					e.printStackTrace();
				}

			}
		});

	}

	private static void startIdentificationThread() {
		Identifier.startIdentificationThread();
	}
}
