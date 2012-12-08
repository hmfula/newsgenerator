package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.coin2012.wikipulse.extraction.utils.ResultParser;
import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.SnippetPage;
import com.coin2012.wikipulse.models.WikiEdit;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class WikipediaResultParser extends ResultParser {

	public static List<WikiEdit> parseResultToEdits(String result, String pageId) {
		JsonParser jsonParser = new JsonParser();
		JsonArray categorymembers = jsonParser.parse(result).getAsJsonObject()
				.get("query").getAsJsonObject().get("pages").getAsJsonObject()
				.get(pageId).getAsJsonObject().getAsJsonArray("revisions");

		List<WikiEdit> edits = new LinkedList<WikiEdit>();
		if (categorymembers != null) {
			Gson gson = createConfiguredGson();
			for (JsonElement jsonElement : categorymembers) {
				WikiEdit edit = gson.fromJson(jsonElement, WikiEdit.class);
				// edit.setContent(jsonElement.getAsJsonObject().get("*").getAsString());
				edits.add(edit);
			}
		}

		return edits;
	}

	public static List<Page> parseResultToTitles(String result) {
		JsonParser jsonParser = new JsonParser();
		JsonArray categorymembers = jsonParser.parse(result).getAsJsonObject()
				.get("query").getAsJsonObject()
				.getAsJsonArray("categorymembers");

		Gson gson = createConfiguredGson();
		List<Page> titles = new LinkedList<Page>();
		for (JsonElement jsonElement : categorymembers) {
			Page title = gson.fromJson(jsonElement, Page.class);
			titles.add(title);
		}

		return titles;
	}

	/**
	 * TODO refactor this to create universal parser
	 * 
	 * @param result
	 * @return list of page snippets
	 */
	public static List<SnippetPage> parseResultToPageSnippets(String result) {
		JsonParser jsonParser = new JsonParser();
		JsonArray matchingSnnipePageArray = jsonParser.parse(result)
				.getAsJsonObject().get("query").getAsJsonObject()
				.getAsJsonArray("search");

		Gson gson = createConfiguredGson();
		List<SnippetPage> pages = new LinkedList<SnippetPage>();
		for (JsonElement jsonElement : matchingSnnipePageArray) {
			SnippetPage page = gson.fromJson(jsonElement, SnippetPage.class);
			page.setPageUrl(page.getTitle());
			pages.add(page);
		}

		return pages;
	}

	// TODO refactor this to create universal parser

	public static List<Page> parseResultToMatchingPages(String result) {
		JsonParser jsonParser = new JsonParser();
		JsonArray matchingPageArray = jsonParser.parse(result)
				.getAsJsonObject().get("query").getAsJsonObject()
				.getAsJsonArray("exturlusage");

		Gson gson = createConfiguredGson();
		List<Page> pages = new LinkedList<Page>();
		for (JsonElement jsonElement : matchingPageArray) {
			Page page = gson.fromJson(jsonElement, Page.class);
			pages.add(page);
		}

		return pages;
	}

	// TODO refactor to remove duplicate code

	public static List<Page> parseResultPages(String result) {
		List<Page> pages = new ArrayList<Page>();
		Page page = null;
		Gson gson = new Gson();
		JsonParser jsonParser = new JsonParser();
		Set<Entry<String, JsonElement>> pageEntryMap = jsonParser.parse(result)
				.getAsJsonObject().get("query").getAsJsonObject().get("pages")
				.getAsJsonObject().entrySet();
		for (Entry<String, JsonElement> entry : pageEntryMap) {
			// System.out.println("key (page id):" + entry.getKey());
			// System.out.println("value: " + entry.getValue());
			page = gson.fromJson(entry.getValue(), Page.class);

			pages.add(page);
			if (page.getImages() != null) {
				for (Page.Image image : page.getImages()) {
					page.setUrl(image.getTitle());
					// imageTitles.add(image.getTitle());
				}
			}

		}
		return pages;
	}

	public static RecentChangesQueryResult parseResultToRecentChangesQueryResult(String result) {
		RecentChangesQueryResult parsedResult = new RecentChangesQueryResult();
		JsonParser jsonParser = new JsonParser();
		Gson gson = createConfiguredGson();
		JsonArray recentchanges = jsonParser.parse(result).getAsJsonObject()
				.get("query").getAsJsonObject()
				.getAsJsonArray("recentchanges");
		for (JsonElement jsonElement : recentchanges) {
			Change change = gson.fromJson(jsonElement, Change.class);
			parsedResult.getChanges().add(change);
		}
		
		String rcstart = jsonParser.parse(result).getAsJsonObject()
				.get("query-continue").getAsJsonObject()
				.get("recentchanges").getAsJsonObject().get("rcstart").getAsString();
		
		parsedResult.setRcstart(TimestampGenerator.generateTimestampFromWikipediaTimestamp(rcstart));
		return parsedResult;
	}
}
