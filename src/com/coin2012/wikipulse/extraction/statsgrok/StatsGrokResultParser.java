package com.coin2012.wikipulse.extraction.statsgrok;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import com.coin2012.wikipulse.conf.WikipulseConstants;
import com.coin2012.wikipulse.extraction.utils.ResultParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class StatsGrokResultParser extends ResultParser {

	public static float parseResultToRelevance(String result) {
		JsonParser jsonParser = new JsonParser();
		JsonObject dailyViews = jsonParser.parse(result).getAsJsonObject().get("daily_views").getAsJsonObject();
		float yesterdaysViews = 0;
		try {
			yesterdaysViews = dailyViews.get(generateTimestampForYesterday()).getAsFloat();
		} catch (Exception e) {
			yesterdaysViews = dailyViews.get(genereateTimestampXDaysFromToday(-2)).getAsFloat();
		}
		
		float totalViews = 0;
		Set<Entry<String, JsonElement>> viewsSet = dailyViews.entrySet();
		for (Entry<String, JsonElement> entry : viewsSet) {
			totalViews = totalViews + entry.getValue().getAsFloat();
		}

		if (totalViews == 0) {
			return WikipulseConstants.YESTERDAY_RELEVANCE_DEFAULT_VALUE;
		} else {
			float relevance_result = yesterdaysViews / totalViews;
			return relevance_result;
		}
	}

	private static String generateTimestampForYesterday() {
		return genereateTimestampXDaysFromToday(-1);
	}

	private static String genereateTimestampXDaysFromToday(int x) {
		String DATE_PATTERN = "yyyy-MM-dd";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, x);
		return dateFormat.format(calendar.getTime());
	}
}
