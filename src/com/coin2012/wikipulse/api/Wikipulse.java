package com.coin2012.wikipulse.api;

import static spark.Spark.get;

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
	 }

	public void init() {
		createRESTRoutes();
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

		get(new Route("/MostReadArticlesInCategory") {
			@Override
			public Object handle(Request request, Response response) {
				String category = "Category:" + request.queryParams("category");
				WikipulseService wikipulseService = new WikipulseServiceImpl();
				response.type("application/json; charset=utf-8");
				String return_set = wikipulseService.getMostReadTitlesForCategory(category);
				return return_set;
			}
		});
	}

}
