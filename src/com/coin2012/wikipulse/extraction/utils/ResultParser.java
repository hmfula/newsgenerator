package com.coin2012.wikipulse.extraction.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class ResultParser {
	
	protected static Gson createConfiguredGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
//		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		return gson;
	}

}
