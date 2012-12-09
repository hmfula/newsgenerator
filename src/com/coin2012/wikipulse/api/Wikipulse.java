package com.coin2012.wikipulse.api;

import static spark.Spark.get;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;


/**
 * Main class. Defines the entry point into the system.
 * 
 * @author harrison mfula
 * @author karsten packmohr
 * @since 10-11-2012
 */
public class Wikipulse implements SparkApplication {

	 public static void main(String[] args) {
		createRESTRoutes();
		createInMemDb();
	 }

	public void init() {
		createRESTRoutes();
		createInMemDb();
//		spark.Spark.setPort(8080);
		
	}

	private static void createRESTRoutes() {
		get(new Route("/News") {
			@Override
			public Object handle(Request request, Response response) {

				String category = "Category:United_States_presidential_election,_2012";
				WikipulseService wikipulseService = new WikipulseServiceImpl();
				response.type("application/json; charset=utf-8");
				return wikipulseService.getNewsForCategory(category);
			}
		});
		
		get(new Route("/Changes") {
			@Override
			public Object handle(Request request, Response response) {
				WikipulseService wikipulseService = new WikipulseServiceImpl();
				response.type("application/json; charset=utf-8");
				return wikipulseService.getRecentChanges();
			}
		});

		get(new Route("/MostReadArticlesInCategory") {
			@Override
			public Object handle(Request request, Response response) {
				String category = "Category:" + request.queryParams("category");
				WikipulseService wikipulseService = new WikipulseServiceImpl();
				response.type("application/json; charset=utf-8");
				String return_set = "callback(" + wikipulseService.getMostReadTitlesForCategory(category) + ")";
				return return_set;
			}
		});
		
		//Example http://localhost:4567/ExernalLinkSearch?url=www.mittromney.com
		//Example can be used for result verification. http://en.wikipedia.org/w/api.php?action=query&list=exturlusage&euquery=www.mittromney.com&eulimit=30&eunamespace=0&format=json
		get(new Route("/ExernalLinkSearch") {
				@Override
				public Object handle(Request request, Response response) {
					String url = request.queryParams("url");
					WikipulseService wikipulseService = new WikipulseServiceImpl();
					response.type("application/json; charset=utf-8");
					String externallyLinkePages = wikipulseService.searchForPagesReferencing(url);
					
					return externallyLinkePages;
				}
			});
		 
	
	       //Example: to search for pages containing text '2012 US Elections'.  http://localhost:4567/FreeTextSearch?&srsearch=2012 US Elections
			get(new Route("/FreeTextSearch") {
					@Override
					public Object handle(Request request, Response response) {
						String searchText = request.queryParams("srsearch");
						WikipulseService wikipulseService = new WikipulseServiceImpl();
						response.type("application/json; charset=utf-8");
						String  snippetPages = "callback(" + wikipulseService.searchForPagesThatMatch(searchText) + ")";
						return snippetPages;
					}

				});
			
			
			// Example: To list images on  Albert Einstein page. http://localhost:4567/FetchPageImages?titles=Albert%20Einstein
			
			get(new Route("/FetchPageImages") {
				
					@Override
					public Object handle(Request request, Response response) {
						
						String imageTitle = request.queryParams("titles");
						WikipulseService wikipulseService = new WikipulseServiceImpl();
						response.type("application/json; charset=utf-8");
						String  pageDetails = wikipulseService.getPageWithImages(imageTitle);
						return pageDetails;
					}

				});
			
	}

	private static void createInMemDb() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			connection.createStatement().execute("CREATE TABLE changes (timestamp varchar(20), pageTitle varchar(255))");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}