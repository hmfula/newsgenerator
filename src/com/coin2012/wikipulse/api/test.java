package com.coin2012.wikipulse.api;

import static spark.Spark.get;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import spark.Request;
import spark.Response;
import spark.Route;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	
		get(new Route("/Categories/:category") {
			@Override
			public Object handle(Request request, Response response) {
				String category = request.params(":category");
				try {
					category = URLDecoder.decode(category, "UTF-8");
					response.type("application/json; charset=utf-8");
					return category;
				} catch (UnsupportedEncodingException e) {;
					response.status(400);
					return response;
				}
			}

		});
	}
}
