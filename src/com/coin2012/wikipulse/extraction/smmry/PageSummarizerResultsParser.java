package com.coin2012.wikipulse.extraction.smmry;

import com.coin2012.wikipulse.extraction.utils.ResultParser;
import com.coin2012.wikipulse.models.Page;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class PageSummarizerResultsParser extends ResultParser{
	public static Page parseResultToSummaryPage(String result) {
		JsonParser jsonParser = new JsonParser();
		Gson gson = createConfiguredGson();
		
		Page page = new  Page();//TODO Fix this to use correct page
		
		JsonElement pageJson = jsonParser.parse(result).getAsJsonObject();
		Page.PageSummary summary = gson.fromJson(pageJson, Page.PageSummary.class);
		page.setPageSummary(summary);
		return page;
	}

}
