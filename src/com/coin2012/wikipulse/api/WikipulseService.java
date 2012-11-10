package com.coin2012.wikipulse.api;

import static spark.Spark.get;

import com.coin2012.wikipulse.identification.Identifiable;
import com.coin2012.wikipulse.identification.Identifier;

import spark.Request;
import spark.Response;
import spark.Route;
/**
 * 
 * Service class - test comment
 *
 */
public class WikipulseService {

	public static void main(String[] args) {

		get(new Route("/News") {
			@Override
			public Object handle(Request request, Response response) {
				String category = "Category:United_States_presidential_election,_2012";
				Identifiable identifier = new Identifier();
				String news = identifier.getNewsForCategory(category);

				return news;
			}
		});

	}

}