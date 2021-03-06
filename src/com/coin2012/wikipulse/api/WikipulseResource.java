package com.coin2012.wikipulse.api;

import static spark.Spark.get;
import static spark.Spark.put;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import com.coin2012.wikipulse.extraction.wikipedia.polling.RecentChangesRunnable;
import com.coin2012.wikipulse.identification.Identifier;

/**
 * This class initializes and starts up the WikipulseResource through the main
 * method to run stand alone on an embedded Jetty server or through the init
 * method when run on a Tomcat server. It defines which rest routes are offered
 * to the frontend and also starts db setups and necessary polling threads.
 * 
 * 
 */
public class WikipulseResource implements SparkApplication {

	private static Logger logger = Logger.getLogger(WikipulseResource.class.toString());

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
	 * Creates all available REST routes.
	 */
	private static void createRESTRoutes() {
		get(new Route("/news") {
			@Override
			public Object handle(Request request, Response response) {
				try {
					String sort = request.queryParams("sort");
					int limit = Integer.valueOf(request.queryParams("limit"));
					response.type("application/json; charset=utf-8");
					return wikipulseService.getNews(sort, limit);
				} catch (Exception e) {
					response.status(400);
					response.body("Query parameters sort and limit have to be set. limit needs to be an integer");
					return response;
				}

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
				try {
					int minChanges = Integer.valueOf(request.queryParams("minchanges"));
					response.type("application/json; charset=utf-8");
					return wikipulseService.getRecentChanges(minChanges);
				} catch (Exception e) {
					response.status(400);
					response.body("Query parameter limit has to be set. limit needs to be an integer");
					return response;
				}
			}
		});

		get(new Route("/categories") {
			@Override
			public Object handle(Request request, Response response) {
				try {
					int limit = Integer.valueOf(request.queryParams("limit"));
					response.type("application/json; charset=utf-8");
					return wikipulseService.getCategories(limit);
				} catch (Exception e) {
					response.status(400);
					response.body("Query parameter limit has to be set. limit needs to be an integer");
					return response;
				}
			}
		});

		get(new Route("/categories/:category") {
			@Override
			public Object handle(Request request, Response response) {
				String category = request.params(":category");
				try {
					category = URLDecoder.decode(category, "UTF-8");
					response.type("application/json; charset=utf-8");
					return wikipulseService.getNewsForCategory(category);
				} catch (UnsupportedEncodingException e) {
					logger.warning("Category: " + category + " could not be decoded");
					response.status(400);
					return response;
				}
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
	}

	/**
	 * Sets up the in memory db and starts the polling needed to offer pages
	 * with recent changes through the /changes route.
	 */
	private static void createInMemDb() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			connection.createStatement().execute(
					"CREATE TABLE changes (timestamp bigint, pageTitle varchar(255), pageid varchar(255), UNIQUE (timestamp, pageTitle, pageid))");
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
					logger.info("Failed to close the connection to the database because of : " + e.getCause());
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * Starts the thread for the identification of news.
	 */
	private static void startIdentificationThread() {
		Identifier.startIdentificationThread();
	}
}
