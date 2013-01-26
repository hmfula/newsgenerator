package com.coin2012.wikipulse.extraction.smmry;

import com.coin2012.wikipulse.extraction.utils.ResultParser;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.PageSummary;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class PageSummarizerResultsParser extends ResultParser{
	public static PageSummary parseResultToSummaryPage(String result) {
		JsonParser jsonParser = new JsonParser();
		Gson gson = createConfiguredGson();
		
		
		JsonElement pageJson = jsonParser.parse(result).getAsJsonObject();
		PageSummary summary = gson.fromJson(pageJson, PageSummary.class);
		return summary;
	}

}
