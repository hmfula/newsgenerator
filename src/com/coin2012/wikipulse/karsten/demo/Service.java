package com.coin2012.wikipulse.karsten.demo;

import static spark.Spark.get;
import spark.Request;
import spark.Response;
import spark.Route;

public class Service {

	public static void main(String[] args) {

		get(new Route("/News") {
			@Override
			public Object handle(Request request, Response response) {
				String category = "Category:United_States_presidential_election,_2012";
				Identifier identifier = new Identifier();
				String news = identifier.getNewsForCategory(category);

				return news;
			}
		});

	}

}