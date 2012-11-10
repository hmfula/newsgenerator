package com.coin2012.wikipulse.api;

import static spark.Spark.get;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Main class. Defines the entry point into the system.
 * 
 * @author harrison mfula
 * @author karsten packmohr
 * @since 10-11-2012
 */
public class Wikipulse {

	public static void main(String[] args) {

		get(new Route("/News") {
			@Override
			public Object handle(Request request, Response response) {
				String category = "Category:United_States_presidential_election,_2012";
				WikipulseService wikipulseService = new WikipulseServiceImpl();
				return wikipulseService.getNewsForCategory(category);
			}
		});

	}

}
