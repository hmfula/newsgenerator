package com.coin2012.wikipulse.api;

import static spark.Spark.get;

import com.coin2012.wikipulse.identification.Identifiable;
import com.coin2012.wikipulse.identification.Identifier;

import spark.Request;
import spark.Response;
import spark.Route;
/**
 * Main class. Defines the entry point into the system.
 * @author harrison mfula
 * @author Karsten packmohr
 * @since 10-11-2012
 */
public class Wikipulse {

	public static void main(String[] args) {

		get(new Route("/News") {
			@Override
			public Object handle(Request request, Response response) {
				String category = "Category:United_States_presidential_election,_2012";
				WikiPulseService wikipulseService = new WikiPulseServiceImpl();
				return  wikipulseService.getNewsForCategory(category);
			}
		});

	}

}
